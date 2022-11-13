package com.betterus.domain.emailcertification.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Email {

    @Id
    @GeneratedValue
    @Column(name = "emailId")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "authCode")
    private String authCode;

    /**
     * 이메일 인증번호 저장용
     */
    public Email(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
    }

    public void changeAuthCode(String authCode){
        this.authCode = authCode;
    }
}
