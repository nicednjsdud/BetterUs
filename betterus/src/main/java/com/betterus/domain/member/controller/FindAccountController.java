/**
 * 작성자 : 정원영
 * 작성 일자 : 2026 - 10 - 30
 * 기능 : 아이디/비밀번호 찾기 화면 및 처리 (M000000006)
 *
 * 이메일 인증코드로 본인을 확인한 뒤 ① 가입 계정의 아이디(닉네임)를 알려주거나
 * ② 임시 비밀번호를 메일로 재발급한다. 값은 화면에 노출하지 않는다.
 */

package com.betterus.domain.member.controller;

import com.betterus.domain.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FindAccountController {

    private final EmailService emailService;

    @GetMapping("/login/findPassword")
    public String findPassword(Model model) {
        model.addAttribute("mailAuthSkip", emailService.isSkipAuth());
        return "login/findpassword";
    }

    /**
     * 아이디 찾기 — 인증코드를 확인한 뒤 가입 계정의 아이디(닉네임)만 돌려준다.
     */
    @PostMapping("/api/member_id_inquiries")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findMemberId(
            @RequestParam("email") String email,
            @RequestParam("authCode") String authCode) {
        Map<String, Object> body = new HashMap<>();
        if (!emailService.verifyCode(email, authCode)) {
            body.put("ok", false);
            body.put("message", "인증코드가 올바르지 않거나 만료되었습니다.");
            return ResponseEntity.ok(body);
        }
        String nickName = emailService.findNickNameByEmail(email);
        if (nickName == null) {
            body.put("ok", false);
            body.put("message", "해당 이메일로 가입된 계정이 없습니다.");
            return ResponseEntity.ok(body);
        }
        body.put("ok", true);
        body.put("nickName", nickName);
        return ResponseEntity.ok(body);
    }

    /**
     * 비밀번호 찾기 — 인증코드를 확인한 뒤 임시 비밀번호를 발급해 메일로 보낸다(화면 미표시).
     */
    @PostMapping("/api/password_resets")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestParam("email") String email,
            @RequestParam("authCode") String authCode) {
        Map<String, Object> body = new HashMap<>();
        if (!emailService.verifyCode(email, authCode)) {
            body.put("ok", false);
            body.put("message", "인증코드가 올바르지 않거나 만료되었습니다.");
            return ResponseEntity.ok(body);
        }
        String temp = emailService.resetPasswordToTemp(email);
        if (temp == null) {
            body.put("ok", false);
            body.put("message", "해당 이메일로 가입된 계정이 없습니다.");
            return ResponseEntity.ok(body);
        }
        body.put("ok", true);
        return ResponseEntity.ok(body);
    }
}
