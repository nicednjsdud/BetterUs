package com.betterus.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberFrm {

    private String nickname;
    private String password;
    private String email;

    private String phone;

    public MemberFrm(String nickname, String password, String email, String phone) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
}
