/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2026 - 10 - 30
 * 기능 : MemberController — 회원가입·회원정보 수정·탈퇴
 */


package com.betterus.domain.member.controller;


import com.betterus.domain.email.service.EmailService;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.dto.MemberEditForm;
import com.betterus.domain.member.dto.MemberJoinForm;
import com.betterus.domain.member.service.MemberService;
import com.betterus.model.Grade;
import com.betterus.model.PasswordConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    /**
     * 작가 리스트 (페이징) — 구독자가 많은 순
     */
    @GetMapping("/list/author")
    public String authorList(@PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            Page<MemberDto> authors = memberService.findAuthorByGrade(pageable);
            setPageModel(model, authors);
            model.addAttribute("authorList", authors);
        } else {
            model.addAttribute("msg", "회원만 보기가 가능합니다.");
            return "main/main";
        }
        return "article/authors";
    }

    /**
     * 작가 서치 리스트 (페이징)
     */
    @GetMapping("/list/author/search")
    public String articleListSearch(HttpServletRequest request, @RequestParam("searchKeyword") String keyword, @PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            Page<MemberDto> searchMemberList = memberService.findSearchMemberList(keyword, pageable);
            setPageModel(model, searchMemberList);
            model.addAttribute("authorList", searchMemberList);
            return "article/authors";
        } else {
            model.addAttribute("msg", "회원만 리스트 접근이 가능합니다.");
            return "main/main";
        }
    }

    private void setPageModel(Model model, Page<?> page) {
        int nowPage = page.getPageable().getPageNumber() + 1;
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", Math.max(nowPage - 4, 1));
        model.addAttribute("endPage", Math.min(nowPage + 5, page.getTotalPages()));
    }

    /**
     * 회원 가입 폼 — 이미 로그인 상태면 메인으로 되돌린다.
     */
    @GetMapping("/signUp")
    public String joinForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("isLogOn"))) {
            return "redirect:/";
        }
        model.addAttribute("joinForm", new MemberJoinForm());
        model.addAttribute("mailAuthSkip", emailService.isSkipAuth());
        return "login/signup";
    }

    /**
     * 닉네임 중복확인 — 사용 가능하면 available=true
     */
    @PostMapping("/api/member_nicknames")
    @ResponseBody
    public Map<String, Object> duplicateNickName(@RequestParam("nickName") String nickName) {
        Map<String, Object> body = new HashMap<>();
        body.put("available", memberService.duplicateCheck(nickName) == 1);
        body.put("nickName", nickName);
        return body;
    }

    /**
     * 회원 가입 진행 — 이메일 인증·중복 확인 후 일반회원으로 등록한다.
     */
    @PostMapping("/signUp")
    public String joinMember(@ModelAttribute("joinForm") MemberJoinForm form, Model model) {
        String nickName = form.getNickname() == null ? "" : form.getNickname().trim();
        String email = form.getEmail() == null ? "" : form.getEmail().trim();
        String password = form.getPassword() == null ? "" : form.getPassword();
        String authCode = form.getAuthCode() == null ? "" : form.getAuthCode().trim();

        String error = validateJoin(nickName, email, password, authCode);
        if (error != null) {
            model.addAttribute("msg", error);
            model.addAttribute("mailAuthSkip", emailService.isSkipAuth());
            return "login/signup";
        }

        Member member = new Member(nickName, password, email, Grade.USER);
        if (form.getPhone() != null && !form.getPhone().trim().isEmpty()) {
            member.changeMemberPhone(form.getPhone().trim());
        }
        int result = memberService.joinMember(member, authCode);
        if (result == 1) {
            return "redirect:/login?signup=done";
        }
        model.addAttribute("msg", "이미 가입된 이메일이거나 닉네임입니다. 다른 값을 사용해 주세요.");
        model.addAttribute("mailAuthSkip", emailService.isSkipAuth());
        return "login/signup";
    }

    private String validateJoin(String nickName, String email, String password, String authCode) {
        // 인증 건너뛰기가 켜져 있으면 인증코드는 받지 않는다.
        if (email.isEmpty() || nickName.isEmpty() || password.isEmpty()
                || (!emailService.isSkipAuth() && authCode.isEmpty())) {
            return "모든 항목을 입력해 주세요.";
        }
        if (nickName.length() < 2 || nickName.length() > 12) {
            return "닉네임은 2~12자로 입력해 주세요.";
        }
        if (!email.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,}$")) {
            return "이메일 형식이 올바르지 않습니다.";
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) {
            return "비밀번호는 8자 이상 영문+숫자 조합이어야 합니다.";
        }
        return null;
    }

    /**
     * 개인 정보 수정 폼 — 본인(세션 회원)만 접근할 수 있다.
     */
    @GetMapping("/editInfo/{memberId}/edit")
    public String changeInfoForm(@PathVariable("memberId") Long memberId, Model model, HttpServletRequest request) {
        Long sessionMemberId = sessionMemberId(request);
        if (sessionMemberId == null) {
            model.addAttribute("msg", "로그인이 필요합니다.");
            return "main/main";
        }
        if (!sessionMemberId.equals(memberId)) {
            model.addAttribute("msg", "본인 정보만 수정할 수 있습니다.");
            return "main/main";
        }
        Member findMember = memberService.findMemberById(memberId);
        if (findMember == null) {
            model.addAttribute("msg", "회원 정보를 찾을 수 없습니다.");
            return "main/main";
        }
        model.addAttribute("memberList", new MemberDto(findMember.getId(), findMember.getNickName(),
                findMember.getPassword(), findMember.getEmail(), findMember.getGrade(), findMember.getUser_info()));
        model.addAttribute("phone", findMember.getPhone());
        model.addAttribute("authorConfirmDate", findMember.getAuthorConfirmDate());
        model.addAttribute("createDate", findMember.getCreateDate() == null ? "" :
                findMember.getCreateDate().toLocalDate().toString());
        model.addAttribute("form", new MemberEditForm());
        return "myPage/mypage(info)";
    }

    /**
     * 개인 정보 수정 저장
     */
    @PostMapping("/editInfo/{memberId}/edit")
    public String changeInfo(@PathVariable("memberId") Long memberId, Model model,
                             @ModelAttribute("form") MemberEditForm form,
                             HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Long sessionMemberId = sessionMemberId(request);
        if (sessionMemberId == null) {
            model.addAttribute("msg", "로그인이 필요합니다.");
            return "main/main";
        }
        if (!sessionMemberId.equals(memberId)) {
            model.addAttribute("msg", "본인 정보만 수정할 수 있습니다.");
            return "main/main";
        }

        int result = memberService.changeMemberInfo(memberId, form);
        if (result == 1) {
            redirectAttributes.addFlashAttribute("msg", "회원정보 수정이 완료되었습니다.");
            return "redirect:/myPage/default";
        }
        String msg;
        if (result == 2) msg = "현재 비밀번호가 일치하지 않습니다.";
        else if (result == 3) msg = "이미 사용 중인 닉네임입니다.";
        else if (result == 4) msg = "이미 사용 중인 이메일입니다.";
        else msg = "수정 중 오류가 발생했습니다. 다시 시도해 주세요.";
        model.addAttribute("msg", msg);
        // 다시 폼을 그리기 위해 회원 정보를 다시 싣는다.
        return changeInfoForm(memberId, model, request);
    }

    /**
     * 회원 탈퇴 — 현재 비밀번호 재확인 후 진행한다.
     */
    @PostMapping("/editInfo/{memberId}/delete")
    public String deleteMember(@PathVariable("memberId") Long memberId, Model model,
                               @RequestParam(value = "currentPassword", required = false) String currentPassword,
                               HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Long sessionMemberId = sessionMemberId(request);
        if (sessionMemberId == null) {
            model.addAttribute("msg", "로그인이 필요합니다.");
            return "main/main";
        }
        if (!sessionMemberId.equals(memberId)) {
            model.addAttribute("msg", "본인만 탈퇴할 수 있습니다.");
            return "main/main";
        }
        Member findMember = memberService.findMemberById(memberId);
        if (findMember == null) {
            model.addAttribute("msg", "회원 정보를 찾을 수 없습니다.");
            return "main/main";
        }
        if (!PasswordConfig.matches(passwordEncoder, currentPassword, findMember.getPassword())) {
            model.addAttribute("msg", "현재 비밀번호가 일치하지 않습니다.");
            return changeInfoForm(memberId, model, request);
        }
        memberService.deleteMember(memberId);
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        redirectAttributes.addFlashAttribute("msg", "회원 탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다.");
        return "redirect:/";
    }

    private Long sessionMemberId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (Long) session.getAttribute("member");
    }
}
