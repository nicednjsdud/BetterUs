/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2022 - 11 - 10
 * 기능 : EmailController
 */


package com.betterus.domain.emailcertification.controller;

import com.betterus.domain.emailcertification.dto.EmailAuthRequestDto;
import com.betterus.domain.emailcertification.service.EmailService;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final MemberRepository memberRepository;

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
