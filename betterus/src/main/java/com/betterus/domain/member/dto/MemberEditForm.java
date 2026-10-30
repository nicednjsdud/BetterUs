/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 06
 * 수정 일자 : 2026 - 10 - 30
 * 기능 : 회원정보 수정 폼 (닉네임·이메일·전화번호·소개글·비밀번호)
 */

package com.betterus.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberEditForm {

    private String nickName;
    private String email;
    private String phone;
    private String info;

    /** 변경할 새 비밀번호 (비우면 변경하지 않음) */
    private String newPassword;

    /** 현재 비밀번호 — 비밀번호를 바꾸거나 탈퇴할 때 본인 확인용 */
    private String currentPassword;
}
