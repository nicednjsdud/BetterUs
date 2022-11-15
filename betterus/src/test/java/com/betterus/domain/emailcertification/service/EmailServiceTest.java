package com.betterus.domain.emailcertification.service;

import com.betterus.domain.emailcertification.domain.Email;
import com.betterus.domain.emailcertification.repository.EmailRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.model.Grade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EmailServiceTest {

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("인증번호 저장")
    void saveAuthCode(){
        String email = "nicednjsdud@naver.com";
        String authCode = "test";
        Email saveEmail = new Email(email,authCode);
        emailRepository.save(saveEmail);
        em.flush();
        em.clear();

        emailService.saveAuthCode("nicednjsdud@naver.com", "test2222");
        Email findEmail = emailRepository.findByEmail("nicednjsdud@naver.com");

        assertThat(findEmail.getAuthCode()).isEqualTo("test2222");
    }

    @Test
    @DisplayName("인증번호 유효 검사")
    void effectAuthCodeCheck(){
        String email = "nicednjsdud@naver.com";
        String authCode = "test";
        Email saveEmail = new Email(email,authCode);
        emailRepository.save(saveEmail);
        em.flush();
        em.clear();

        int result = emailService.effectAuthCodeCheck("test");

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일 전송 후 임시 비밀번호로 변경")
    void changeTempPassword(){
        String authCode = "test";
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);
        em.flush();
        em.clear();

        emailService.saveTempPassword("nicednjsdud@gmail.com",authCode);
        Member findMember = memberRepository.findByEmail("nicednjsdud@gmail.com");

        assertThat(findMember.getPassword()).isEqualTo("test");
    }
}