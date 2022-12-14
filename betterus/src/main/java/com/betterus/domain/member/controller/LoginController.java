/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 04
 * 수정 일자 :
 * 기능 : LoginController
 */

package com.betterus.domain.member.controller;

import com.betterus.domain.email.dto.EmailAuthRequestDto;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.dto.MemberLoginForm;
import com.betterus.domain.member.service.MemberService;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String login(@ModelAttribute("loginForm") MemberLoginForm form, Model model, HttpServletRequest request) {

        String memberEmail = form.getEmail();
        String memberPassword = form.getPassword();
        Member loginMember = memberService.loginConfirm(memberEmail, memberPassword);

        // 회원가입 내역이 있으면
        if (loginMember != null) {
            HttpSession session = request.getSession();
            MemberDto memberDto = new MemberDto(loginMember.getId(), loginMember.getNickName(), loginMember.getGrade());
            session.setAttribute("member", memberDto.getId());
            session.setAttribute("isLogOn", true);
            if(loginMember.getGrade() == Grade.ADMIN) session.setAttribute("admin", true);
            return "main/main";
        } else {
            String msg = "입력하신 아이디 혹은 패스워드가 틀립니다.";
            model.addAttribute("msg", msg);
            return "login/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/login/findPassword")
    public String findPassword(Model model,EmailAuthRequestDto emailAuthRequestDto) {
        model.addAttribute("emailForm", emailAuthRequestDto);
        return "login/findpassword";
    }

}
