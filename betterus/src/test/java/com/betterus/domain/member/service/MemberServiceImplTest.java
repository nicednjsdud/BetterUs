package com.betterus.domain.member.service;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberEditForm;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.model.Grade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;



    @Test
    @DisplayName("로그인 검증")
    public void loginConfirm(){
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberService.loginConfirm("nicednjsdud@gmail.com", "123123");

        assertThat(findMember).isNotNull();
    }

    @Test
    @DisplayName("로그인 검증실패")
    public void loginConfirmFail(){
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberService.loginConfirm("nice@gmail.com", "123123");

        assertThrows(NullPointerException.class, () -> findMember.getId());
    }

    @Test
    @DisplayName("회원가입 검증")
    public void singUpMember(){
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findByNickName("MemberA");

        assertThat(findMember.getNickName()).isEqualTo(saveMember.getNickName());
        assertThat(findMember.getPassword()).isEqualTo(saveMember.getPassword());
        assertThat(findMember.getUser_info()).isEqualTo(null);
    }

    @Test
    @DisplayName("멤버 아이디로 조회하기")
    public void findMemberById(){
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberService.findMemberById(saveMember.getId());

        assertThat(findMember.getNickName()).isEqualTo(saveMember.getNickName());
        assertThat(findMember.getPassword()).isEqualTo(saveMember.getPassword());
        assertThat(findMember.getUser_info()).isEqualTo(null);
    }

    @Test
    @DisplayName("회원 정보 변경 - 닉네임변경")
    public void changeInfo(){
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);
        Long memberId = saveMember.getId();
        em.flush();
        em.clear();

        MemberEditForm changeNickNameForm = new MemberEditForm(null,"MemberB",null,null,"닉네임변경");
        int result = memberService.changeMemberInfo(memberId, changeNickNameForm);

        Member findMember = em.find(Member.class, memberId);

        assertThat(findMember.getNickName()).isEqualTo("MemberB");
        assertThat(result).isEqualTo(1);
        }

}