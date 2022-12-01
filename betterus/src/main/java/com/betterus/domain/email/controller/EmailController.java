/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2022 - 11 - 10
 * 기능 : EmailController
 */


package com.betterus.domain.email.controller;

import com.betterus.domain.email.dto.EmailAuthRequestDto;
import com.betterus.domain.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("mail/mailConfirm")
    public String mailConfirm(@RequestBody EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException {
        String action = "mailConfirm";
        String authCode = emailService.sendEmail(emailDto.getEmail(),action);
        emailService.saveAuthCode(emailDto.getEmail(),authCode);
        return authCode;
    }

    @PostMapping("mail/sendTempPassword")
    public String sendTempPassword(@RequestBody EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException{
        String action = "sendTempPassword";
        String authCode = emailService.sendEmail(emailDto.getEmail(),action);
        emailService.saveTempPassword(emailDto.getEmail(),authCode);
        return authCode;
    }
}
