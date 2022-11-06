/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 04
 * 수정 일자 :
 * 기능 : LoginController
 */

package com.betterus.domain.member.controller;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.dto.MemberFindForm;
import com.betterus.domain.member.dto.MemberJoinForm;
import com.betterus.domain.member.dto.MemberLoginForm;
import com.betterus.domain.member.service.MemberService;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new MemberLoginForm());
        return "login/login";
    }

    @PostMapping("/login/confirm")
    public String login(MemberLoginForm form, Model model, HttpServletRequest request) {

        String memberEmail = form.getEmail();
        String memberPassword = form.getPassword();
        Member loginMember = memberService.loginConfirm(memberEmail, memberPassword);

        // 회원가입 내역이 있으면
        if (loginMember != null) {
            HttpSession session = request.getSession();
            MemberDto memberDto = new MemberDto(loginMember.getId(), loginMember.getNickName());
            session.setAttribute("member", memberDto);
            session.setAttribute("isLogOn", true);
            String msg = "안녕하세요. " + memberDto.getNickName() + "님";
            model.addAttribute("msg", msg);
            return "redirect:/";
        } else {
            String msg = "입력하신 아이디 혹은 패스워드가 틀립니다.";
            model.addAttribute("msg", msg);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        session.removeAttribute("member");
        session.removeAttribute("isLogOn");
        return "redirect:/";
    }

    @GetMapping("/login/findPassword")
    public String findPassword() {
        return "findPassword";
    }

    @PostMapping("/login/sendEmail")
    public String sendEmail(MemberFindForm form) {
        // 메일 api
        // 임시 비밀번호
        // 이메일 인증   // 중복시 메세지 입력
        return null;
    }

    @GetMapping("/signUp")
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new MemberJoinForm());
        return "signUp";
    }

    @PostMapping("/signUp")
    public String joinMember(MemberJoinForm form, Model model) {
        String msg = "";
        Member member = new Member(form.getNickname(), form.getPassword(), form.getEmail(), Grade.USER);
        int result = memberService.joinMember(member);
        if (result == 1) {
            msg = "회원가입이 완료되었습니다.";
        }
        else{
            msg = "회원가입에 실패하였습니다. 다시 시도해주세요.";
        }
        model.addAttribute("msg",msg);
        return "redirect:/";
    }

    @PostMapping("/signUp/duplicateNickName")
    public String duplicateNickName(@RequestParam("nickName") String nickName,Model model){

        int result = memberService.duplicateCheck(nickName);
        model.addAttribute("result",result);
        return "signup";
    }


}
