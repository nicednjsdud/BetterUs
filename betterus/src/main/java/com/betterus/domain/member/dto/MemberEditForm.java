/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 06
 * 수정 일자 :
 * 기능 : 멤버 수정 폼
 */

package com.betterus.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberEditForm {

    private String info;
    private String nickName;
    private String prePassword;
    private String afterPassword;
    private String action;
}
