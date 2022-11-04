/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 :
 * 기능 : 멤버 service 구현체
 */


package com.betterus.domain.member.service;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /**
     * 로그인 확인
     */
    @Override
    public Member loginConfirm(String memberEmail, String memberPassword) {
        Member findMember = memberRepository.findByEmailAndPassword(memberEmail, memberPassword);
        if (findMember != null) return findMember;
        else return null;
    }

    /**
     * 회원 가입
     */
    @Override
    public int joinMember(Member member) {
        Member duplicateEmail = memberRepository.findByEmail(member.getEmail());
        if (duplicateEmail== null){
            Member joinMember = memberRepository.save(member);
            return 1;
        }
        else return 0;
    }

    /**
     * 닉네임 중복확인
     */
    @Override
    public int duplicateCheck(String nickName) {
        Member findMember = memberRepository.findByNickName(nickName);
        if (findMember != null) return 1;
        else return 0;
    }
}
