/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 04
 * 수정 일자 :
 * 기능 : LoginController
 */

package com.betterus.domain.member.controller;

import com.betterus.domain.member.domain.Member;
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
    public String login(Model model, HttpServletRequest request) {
        // 이미 로그인한 상태로 이 화면에 들어오면 메인으로 되돌린다.
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("isLogOn"))) {
            return "redirect:/";
        }
        model.addAttribute("loginForm", new MemberLoginForm());
        return "login/login";
    }

    @PostMapping("/login/confirm")
    public String login(@ModelAttribute("loginForm") MemberLoginForm form, Model model, HttpServletRequest request) {

        String memberEmail = form.getEmail() == null ? "" : form.getEmail().trim();
        String memberPassword = form.getPassword() == null ? "" : form.getPassword();

        // 빈 값 검사 — 어느 항목이 비었는지 알려 준다.
        if (memberEmail.isEmpty() || memberPassword.isEmpty()) {
            model.addAttribute("msg", "이메일과 비밀번호를 모두 입력해 주세요.");
            return "login/login";
        }

        Member loginMember = memberService.loginConfirm(memberEmail, memberPassword);

        // 회원가입 내역이 있으면
        if (loginMember != null) {
            HttpSession session = request.getSession();
            session.setAttribute("member", loginMember.getId());
            session.setAttribute("isLogOn", true);
            // 관리자 등급이면 관리자 여부를 함께 세팅하고, 아니면 이전 값이 남지 않게 지운다.
            if (loginMember.getGrade() == Grade.ADMIN) {
                session.setAttribute("admin", true);
            } else {
                session.removeAttribute("admin");
            }
            // 관리자는 관리자 화면으로, 그 밖의 등급은 메인으로 보낸다.
            if (loginMember.getGrade() == Grade.ADMIN) {
                return "redirect:/myPage/admin/memberInfo";
            }
            return "redirect:/";
        } else {
            // 계정 존재 여부를 노출하지 않도록 실패 문구를 하나로 유지한다.
            model.addAttribute("msg", "이메일 또는 비밀번호가 올바르지 않습니다.");
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

}
