/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 :
 * 기능 : 멤버 service interface
 */

package com.betterus.domain.member.service;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberEditForm;

public interface MemberService {

    Member loginConfirm(String memberEmail, String memberPassword);

    int joinMember(Member member,String authCode);

    int duplicateCheck(String nickName);

    Member findMemberById(Long memberId);

    int changeMemberInfo(Long memberId, MemberEditForm form);
}
