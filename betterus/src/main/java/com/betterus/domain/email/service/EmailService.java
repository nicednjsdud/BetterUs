/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2022 - 11 - 10
 * 기능 : Email service logic
 */


package com.betterus.domain.email.service;

import com.betterus.domain.email.domain.Email;
import com.betterus.domain.email.repository.EmailRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private String authNum; // 랜덤 인증 코드

    private final EntityManager em;

    private final EmailRepository emailRepository;

    private final MemberRepository memberRepository;

    /**
     * 랜덤 코드 8자리 발송
     */
    public void createCode(){
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++){
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    /**
     * 메일 폼
     */
    @Transactional
    public MimeMessage createEmailForm(String email,String action) throws MessagingException, UnsupportedEncodingException {

        createCode(); //인증 코드 생성
        String setFrom = "betterus-info@naver.com";
        String toEmail = email; //받는 사람
        String title = "BetterUs 회원가입 인증 번호";

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title);
        message.setFrom(setFrom);
        message.setText(setContext(authNum,action), "utf-8", "html");

        return message;
    }

    /**
     * 메일 전송 (회원가입 인증번호, 임시비밀번호)
     */
    @Transactional
    public String sendEmail(String toEmail,String action) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createEmailForm(toEmail,action);
        emailSender.send(emailForm);
        return authNum;
    }

    /**
     * 타임리프를 이용한 context 설정
     */
    public String setContext(String code,String action) {
        Context context = new Context();
        context.setVariable("code", code);
        if(action.equals("mailConfirm")) return templateEngine.process("mail",context);
        else if(action.equals("sendTempPassword")) return templateEngine.process("tempPassword",context);
        else return null;
    }

    /**
     * 회원가입 인증번호 저장
     */
    @Transactional
    public String saveAuthCode(String email,String authCode){
        Email findEmail = emailRepository.findByEmail(email);
        Email saveEmail;
        if(findEmail !=null){
            // 새로운 인증 요청이 있을경우
            findEmail.changeAuthCode(authCode);
        }
        else{
            saveEmail = new Email(email,authCode);
            emailRepository.save(saveEmail);
        }

        return email;
    }

    /**
     * 회원가입 인증번호 유효성 체크
     */
    @Transactional
    public int effectAuthCodeCheck(String authCode){
        Email findEmail = emailRepository.findByAuthCode(authCode);
        if(findEmail !=null){
            emailRepository.delete(findEmail);
            return 1;
        }
        else return 0;
    }

    /**
     * 임시 비밀번호 저장
     */
    @Transactional
    public void saveTempPassword(String toEmail,String authCode) {
        Member findMember = memberRepository.findByEmail(toEmail);
        if(findMember != null){
            findMember.changeMemberPassword(authCode);
        }
    }
}
