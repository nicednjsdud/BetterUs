/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 :
 * 기능 : 멤버 service 구현체
 */


package com.betterus.domain.member.service;

import com.betterus.domain.emailcertification.domain.Email;
import com.betterus.domain.emailcertification.repository.EmailRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberEditForm;
import com.betterus.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final EmailRepository emailRepository;

    private final EntityManager em;

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
    @Transactional
    public int joinMember(Member member, String authCode) {
        Member duplicateEmail = memberRepository.findByEmail(member.getEmail());
        Email findEmail = emailRepository.findByEmail(member.getEmail());

        if (duplicateEmail == null && findEmail.getAuthCode().equals(authCode)) {
            Member joinMember = memberRepository.save(member);
            return 1;
        } else return 0;
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

    /**
     * 멤버 아이디로 찾기
     */
    @Override
    public Member findMemberById(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        return findMember.orElse(null);
    }

    /**
     * 멤버 정보 변경
     */

    // 변경감지안됨 확인
    @Override
    @Transactional
    public int changeMemberInfo(Long memberId, MemberEditForm form) {
        Member member = em.find(Member.class, memberId);
        String action = form.getAction();
        if (Objects.equals(action, "닉네임변경")) {
            member = new Member(form.getNickName(), member.getPassword(), member.getGrade(), member.getUser_info());
            return 1;
        } else if (Objects.equals(action, "비밀변호변경")) {
            Member findMemberByPrePassword = memberRepository.findByPassword(memberId);
            if (findMemberByPrePassword.getPassword().equals(form.getPrePassword())) {
                member = new Member(member.getNickName(), form.getAfterPassword(), member.getGrade(), member.getUser_info());
                return 1;
            } else {
                // 기존 비밀번호와 현재 적은 비밀번호가 다름
                return 2;
            }
        } else if (Objects.equals(action, "회원소개변경")) {
            member = new Member(member.getNickName(), member.getPassword(), member.getGrade(), form.getInfo());
            return 1;
        }

        return 0;
    }
}
