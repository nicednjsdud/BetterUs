/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2026 - 10 - 30
 * 기능 : 이메일 인증코드·임시 비밀번호 발송/검증 (회원가입·아이디/비밀번호 찾기 공용)
 */

package com.betterus.domain.email.service;

import com.betterus.domain.email.domain.Email;
import com.betterus.domain.email.repository.EmailRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    /** 재발송 쿨다운(초) — 설계의 「재발송」 규칙 */
    public static final int COOLDOWN_SECONDS = 60;

    /**
     * 이메일 인증 건너뛰기 — true 면 인증코드를 메일로 보내지 않고 어떤 코드든 통과시킨다.
     * 메일 계정 없이 화면을 확인하는 개발 환경용이며, 설정값 app.mail.auth.skip 으로 켠다.
     * 운영에서는 반드시 false(기본값)여야 한다.
     */
    @Value("${app.mail.auth.skip:false}")
    private boolean skipAuth;

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    /** 인증 건너뛰기가 켜져 있는지 — 화면·가입 처리에서 함께 본다. */
    public boolean isSkipAuth() {
        return skipAuth;
    }

    private static final SecureRandom RANDOM = new SecureRandom();
    /** 이메일별 마지막 발송 시각(밀리초) — 서버 재시작 시 초기화된다(별도 컬럼 없음). */
    private static final Map<String, Long> LAST_SENT_AT = new ConcurrentHashMap<>();

    /**
     * 인증코드 발송 — 6자리 숫자 코드를 만들어 Email 테이블에 저장하고 메일을 보낸다.
     * 메일 발송이 실패해도(로컬 개발 등) 코드는 저장되며, 서버 로그에 개발용으로 남긴다.
     */
    @Transactional
    public String sendAuthCode(String email) {
        String code = randomNumberCode(6);
        Email findEmail = emailRepository.findByEmail(email);
        if (findEmail != null) {
            findEmail.changeAuthCode(code);
        } else {
            emailRepository.save(new Email(email, code));
        }
        if (skipAuth) {
            log.info("[인증 건너뛰기] 메일을 보내지 않았다 — {} (코드 {})", email, code);
        } else {
            sendMail(email, "[Better Us] 이메일 인증코드 안내", buildCodeBody(code));
        }
        LAST_SENT_AT.put(email, System.currentTimeMillis());
        return code;
    }

    /** 남은 쿨다운(초). 0이면 바로 발송 가능. */
    public int remainingCooldown(String email) {
        Long last = LAST_SENT_AT.get(email);
        if (last == null) return 0;
        long passed = (System.currentTimeMillis() - last) / 1000L;
        long remain = COOLDOWN_SECONDS - passed;
        return remain > 0 ? (int) remain : 0;
    }

    /** 입력한 인증코드가 최신 저장값과 같은지 확인한다. 건너뛰기가 켜져 있으면 항상 통과. */
    public boolean verifyCode(String email, String code) {
        if (skipAuth) return email != null && !email.trim().isEmpty();
        if (email == null || code == null) return false;
        Email findEmail = emailRepository.findByEmail(email);
        return findEmail != null && code.equals(findEmail.getAuthCode());
    }

    /** 인증코드 무효화(검증 성공 후 재사용 방지). */
    @Transactional
    public void invalidate(String email) {
        Email findEmail = emailRepository.findByEmail(email);
        if (findEmail != null) emailRepository.delete(findEmail);
    }

    /**
     * 임시 비밀번호 재발급 — 10자리 임시 비밀번호를 만들어 BCrypt 해시로 저장하고 메일로 보낸다.
     * 값은 화면·응답에 노출하지 않는다(메일로만).
     * @return 가입된 회원이 없으면 null
     */
    @Transactional
    public String resetPasswordToTemp(String email) {
        Member findMember = memberRepository.findByEmail(email);
        if (findMember == null) return null;
        String tempPassword = randomPassword(10);
        findMember.changeMemberPassword(passwordEncoder.encode(tempPassword));
        if (skipAuth) {
            log.info("[인증 건너뛰기] 임시 비밀번호를 메일로 보내지 않았다 — {} (임시 비밀번호 {})", email, tempPassword);
        } else {
            sendMail(email, "[Better Us] 임시 비밀번호 안내", buildTempPasswordBody(tempPassword));
        }
        invalidate(email);
        return tempPassword;
    }

    /** 가입된 이메일인지 확인한다(아이디 찾기·비밀번호 찾기 판정용). */
    public boolean isRegistered(String email) {
        return memberRepository.findByEmail(email) != null;
    }

    /** 이메일로 가입한 계정의 아이디(닉네임)만 돌려준다. 없으면 null. */
    public String findNickNameByEmail(String email) {
        Member findMember = memberRepository.findByEmail(email);
        return findMember == null ? null : findMember.getNickName();
    }

    // ---------------------------------------------------------------------

    private void sendMail(String to, String subject, String html) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            emailSender.send(message);
        } catch (Exception e) {
            // 로컬 개발 등 메일 서버를 쓸 수 없는 환경에서는 흐름이 끊기지 않도록 메일 내용을 로그에 남긴다.
            log.warn("[개발용] 메일 발송 실패 — {} (사유: {})", to, e.getMessage());
            if (environment.acceptsProfiles(Profiles.of("local"))) {
                log.warn("[개발용] 메일 내용: {}", html.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim());
            }
        }
    }

    private String buildCodeBody(String code) {
        return "<div style='font-family:sans-serif'>"
                + "<h2>Better Us 이메일 인증코드</h2>"
                + "<p>아래 6자리 코드를 화면에 입력해 주세요.</p>"
                + "<p style='font-size:24px;font-weight:bold;letter-spacing:4px'>" + code + "</p>"
                + "</div>";
    }

    private String buildTempPasswordBody(String tempPassword) {
        return "<div style='font-family:sans-serif'>"
                + "<h2>Better Us 임시 비밀번호</h2>"
                + "<p>아래 임시 비밀번호로 로그인한 뒤, 마이페이지에서 비밀번호를 꼭 바꿔 주세요.</p>"
                + "<p style='font-size:20px;font-weight:bold'>" + tempPassword + "</p>"
                + "</div>";
    }

    private String randomNumberCode(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append(RANDOM.nextInt(10));
        return sb.toString();
    }

    private String randomPassword(int length) {
        final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        return sb.toString();
    }
}
