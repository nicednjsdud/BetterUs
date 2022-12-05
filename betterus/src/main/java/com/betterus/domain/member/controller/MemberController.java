/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2022 = 11 - 22
 * 기능 : MemberController
 */


package com.betterus.domain.member.controller;


import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.email.service.EmailService;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.dto.MemberEditForm;
import com.betterus.domain.member.dto.MemberJoinForm;
import com.betterus.domain.member.service.MemberService;
import com.betterus.domain.mypage.repository.MyPageRepository;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    private final EmailService emailService;

    /**
     * 작가 리스트 (패이징)
     * 구독자가 많은 수 대로
     */
    @GetMapping("/list/author")
    public String authorList(@PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model, HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            Page<MemberDto> authors = memberService.findAuthorByGrade(pageable);
            int nowPage = authors.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, authors.getTotalPages());
            model.addAttribute("authorList", authors);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        } else {
            msg = "회원만 보기가 가능합니다.";
            model.addAttribute("msg", msg);
            return "main/main";
        }
        return "article/authors";
    }

    /**
     * 서치 목록 리스트 (패이징) 진행중
     */
    @GetMapping("/list/author/search")
    public String articleListSearch(HttpServletRequest request,@RequestParam("searchKeyword") String keyword, @PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        String msg = "";
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            Page<MemberDto> searchMemberList = memberService.findSearchMemberList(keyword, pageable);
            int nowPage = searchMemberList.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, searchMemberList.getTotalPages());
            model.addAttribute("authorList", searchMemberList);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "article/authors";
        }else {
            msg = "회원만 리스트 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "main/main";
        }
    }

    /**
     * 회원 가입 폼
     */
    @GetMapping("/signUp")
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new MemberJoinForm());
        return "login/signup";
    }

    /**
     * 회원 가입 진행
     */
    @PostMapping("/signUp")
    public String joinMember(@ModelAttribute("joinForm")MemberJoinForm form, Model model) {
        String msg = "";
        Member member = new Member(form.getNickname(), form.getPassword(), form.getEmail(), Grade.USER);
        int result = memberService.joinMember(member, form.getAuthCode());
        if (result == 1) {
            msg = "회원가입이 완료되었습니다.";
        } else {
            msg = "회원가입에 실패하였습니다. 다시 시도해주세요.";
        }
        model.addAttribute("msg", msg);
        return "redirect:/";
    }

    @PostMapping("/signUp/duplicateNickName")
    public int duplicateNickName(@RequestParam("nickName") String nickName, Model model) {
        int result = memberService.duplicateCheck(nickName);
        model.addAttribute("result", result);
        return result;
    }

    /**
     * 확인 후 진행
     */
    @PostMapping("/signUp/effectAuthCode")
    public String effectAuthCode(@RequestParam("authCode") String authCode, Model model) {
        String msg = "";
        int result = emailService.effectAuthCodeCheck(authCode);
        if (result == 1) {
            msg = "이메일 인증이 완료되었습니다.";
        } else {
            msg = "이메일 인증번호가 다릅니다.";
        }
        model.addAttribute("result", result);
        model.addAttribute("msg", msg);
        return "signup";
    }

    /**
     * 개인 정보 수정 폼
     */
    @GetMapping("/editInfo/{memberId}/edit")
    public String changeInfoForm(@PathVariable("memberId") Long memberId, Model model, MemberEditForm form) {
        Member findMember = memberService.findMemberById(memberId);
        if (findMember != null) {
            MemberDto memberDto = new MemberDto(findMember.getId(), findMember.getNickName(), findMember.getPassword(), findMember.getEmail(), findMember.getGrade(), findMember.getUser_info());
            model.addAttribute("memberList", memberDto);
            model.addAttribute("form", form);
        }
        return "myPage/mypage(info)";
    }

    /**
     * 개인 정보 수정
     */
    @PostMapping("/editInfo/{memberId}/edit")
    public String changeInfo(@PathVariable("memberId") Long memberId, Model model, MemberEditForm form) {
        String msg;
        int result = memberService.changeMemberInfo(memberId, form);
        switch (result) {
            case 0:
                msg = "오류가 발생하였습니다. 다시 시도해주세요.";
                model.addAttribute("msg", msg);
                return "/editInfo/{memberId}/edit";
            case 1:
                msg = "수정이 완료되었습니다.";
                model.addAttribute("msg", msg);
                return "redirect:/";
            case 2:
                msg = "입력하신 비밀번호가 틀립니다.";
                model.addAttribute("msg", msg);
                return "/editInfo/{memberId}/edit";
            default:
                return "redirect:/";
        }
    }
}
