/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2022 - 11 - 10
 * 기능 : EmailController
 */


package com.betterus.domain.email.controller;

import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.email.dto.EmailAuthRequestDto;
import com.betterus.domain.email.service.EmailService;
import com.betterus.domain.member.dto.MemberJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/mail/mailConfirm")
    public void mailConfirm(@RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {
        System.out.println(email);
        String action = "mailConfirm";
        String authCode = emailService.sendEmail(email, action);
        emailService.saveAuthCode(email, authCode);
    }

    @PostMapping("/mail/sendTempPassword")
    public String sendTempPassword(@ModelAttribute("emailForm") EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException {
        String action = "sendTempPassword";
        String authCode = emailService.sendEmail(emailDto.getEmail(), action);
        emailService.saveTempPassword(emailDto.getEmail(), authCode);
        return "redirect:login/login";
    }
}
