/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 :
 * 기능 : 멤버 회원가입 폼용
 */

package com.betterus.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberJoinForm {

    private String email;
    private String nickname;
    private String password;
}
