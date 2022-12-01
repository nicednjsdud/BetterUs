package com.betterus.domain.email.repository;

import com.betterus.domain.email.domain.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class EmailRepositoryTest {

    @Autowired
    EmailRepository emailRepository;

    @Test
    @DisplayName("이메일로 인증번호 찾기")
    void findAuthCodeByEmail(){
        String email = "nicednjsdud@naver.com";
        String authCode = "test";
        Email saveEmail = new Email(email,authCode);
        emailRepository.save(saveEmail);

        Email findEmail = emailRepository.findByEmail("nicednjsdud@naver.com");

        assertThat(findEmail.getEmail()).isEqualTo(email);
        assertThat(findEmail.getAuthCode()).isEqualTo(authCode);
    }

    @Test
    @DisplayName("이메일로 인증번호 찾기 실패")
    void findAuthCodeByEmailFail(){
        String email = "nicednjsdud@naver.com";
        String authCode = "test";
        Email saveEmail = new Email(email,authCode);
        emailRepository.save(saveEmail);

        Email findEmail = emailRepository.findByEmail("nicednjsdud2222@naver.com");

        assertThat(findEmail).isNull();
    }

    @Test
    @DisplayName("인증번호로 이메일 찾기")
    void findEmailByAuthCode(){
        String email = "nicednjsdud@naver.com";
        String authCode = "test";
        Email saveEmail = new Email(email,authCode);
        emailRepository.save(saveEmail);

        Email findEmail = emailRepository.findByAuthCode("test");

        assertThat(findEmail.getEmail()).isEqualTo(email);
        assertThat(findEmail.getAuthCode()).isEqualTo(authCode);
    }

    @Test
    @DisplayName("인증번호로 이메일 찾기 실패")
    void findEmailByAuthCodeFail(){
        String email = "nicednjsdud@naver.com";
        String authCode = "test";
        Email saveEmail = new Email(email,authCode);
        emailRepository.save(saveEmail);

        Email findEmail = emailRepository.findByEmail("test232");

        assertThat(findEmail).isNull();
    }
}