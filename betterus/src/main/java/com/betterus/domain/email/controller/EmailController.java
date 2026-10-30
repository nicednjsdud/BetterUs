/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2026 - 10 - 30
 * 기능 : 이메일 인증코드 발송/확인 API (회원가입·아이디/비밀번호 찾기 공용)
 */

package com.betterus.domain.email.controller;

import com.betterus.domain.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final Environment environment;

    /**
     * 인증코드 발송 — 6자리 코드를 메일로 보내고 남은 쿨다운(초)을 돌려준다.
     * 로컬 개발 환경에서는 메일을 쓸 수 없으므로 화면 확인용 코드를 함께 돌려준다(devCode).
     */
    @PostMapping("/api/email_auth_codes")
    public ResponseEntity<Map<String, Object>> sendAuthCode(@RequestParam("email") String email) {
        Map<String, Object> body = new HashMap<>();
        String code = emailService.sendAuthCode(email);
        body.put("sent", true);
        body.put("email", email);
        body.put("cooldownSeconds", EmailService.COOLDOWN_SECONDS);
        if (environment.acceptsProfiles(org.springframework.core.env.Profiles.of("local"))) {
            // 개발 편의용 — 운영 프로파일에서는 채우지 않는다.
            body.put("devCode", code);
        }
        return ResponseEntity.ok(body);
    }

    /**
     * 인증코드 확인 — 저장된 코드와 대조한다.
     */
    @PostMapping("/api/email_auth_code_verifications")
    public ResponseEntity<Map<String, Object>> verifyAuthCode(
            @RequestParam("email") String email,
            @RequestParam("authCode") String authCode) {
        Map<String, Object> body = new HashMap<>();
        boolean verified = emailService.verifyCode(email, authCode);
        body.put("verified", verified);
        body.put("email", email);
        return ResponseEntity.ok(body);
    }
}
