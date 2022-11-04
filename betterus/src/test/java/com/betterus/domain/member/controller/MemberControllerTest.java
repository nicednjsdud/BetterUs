package com.betterus.domain.member.controller;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.domain.member.service.MemberService;
import com.betterus.model.Grade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberControllerTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    @DisplayName("회원가입")
    public void join() {
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);
    }

    @Test
    @DisplayName("로그인 검증 테스트")
    public void loginConfirm() {

        Member loginMember = memberService.loginConfirm("nicednjsdud@gmail.com", "123123");

        MemberDto memberDto = new MemberDto(loginMember.getId(), loginMember.getNickName());
        String msg = "안녕하세요. " + memberDto.getNickName() + "님";
        // session 테스트는 나중에 공부한 후 진행
        assertThat(msg).isEqualTo("안녕하세요. MemberA님");

    }

    @Test
    @DisplayName("로그인 검증 테스트 실패")
    public void loginConfirmFail() {

        Member loginMember = memberService.loginConfirm("nicednjsdud@gmail23.com", "123123");
        String msg = "";
        if (loginMember != null) {
            MemberDto memberDto = new MemberDto(loginMember.getId(), loginMember.getNickName());

        } else {
            msg = "입력하신 아이디 혹은 패스워드가 틀립니다.";
        }
        // session 테스트는 나중에 공부한 후 진행

        assertThrows(NullPointerException.class, () -> loginMember.getId());
        assertThat(msg).isEqualTo("입력하신 아이디 혹은 패스워드가 틀립니다.");
    }

    @Test
    @DisplayName("회원가입 검증 테스트")
    public void signUp() {
        String msg;
        Member member = new Member("MemberB", "123123123", "nicednjsdud12@gmail.com", Grade.ADMIN);

        int result = memberService.joinMember(member);
        if (result == 1) {
            msg = "회원가입이 완료되었습니다.";
        } else {
            msg = "회원가입에 실패하였습니다. 다시 시도해주세요.";
        }
        assertThat(msg).isEqualTo("회원가입이 완료되었습니다.");

    }

    @Test
    @DisplayName("회원가입 검증 실패 테스트")
    public void signUpFail() {
        String msg = "";
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        int result = memberService.joinMember(member);
        if (result == 1) {
            msg = "회원가입이 완료되었습니다.";
        } else {
            msg = "회원가입에 실패하였습니다. 다시 시도해주세요.";
        }
        assertThat(msg).isEqualTo("회원가입에 실패하였습니다. 다시 시도해주세요.");
    }
}