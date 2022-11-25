/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 04
 * 수정 일자 : 2022 - 11 - 25
 * 기능 : 마이페이지 컨트롤러
 */

package com.betterus.domain.mypage.controller;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 마이페이지 (유저,작가) default (list - 최신순)
     */
    @GetMapping("/myPage/default")
    public String myPage_user(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            Map<Object, Object> myPage = myPageService.findMyPageDefault(member);
            model.addAttribute("myPage", myPage);
            return "/myPage/myPage(info)";
        } else {
            String msg = "회원만 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }

    /**
     * 마이페이지 (관리자) default (list - 최신순)
     */
    @GetMapping("/myPage/admin/memberInfo")
    public String myPage_admin(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            Map<Object, Object> myPage = myPageService.findMyPageDefault(member);
            model.addAttribute("myPage", myPage);
            return "/myPage/myPage(info)";
        } else {
            String msg = "회원만 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }

    /**
     * 작가 상세 페이지 (패이징) 진행중
     */
    @GetMapping("/list/authorPage/{authorId}/default")
    public String authorInfo(@PathVariable("authorId") Long authorId, Model model, HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            if (member.getId() == authorId) {
                // 만약 자신의 서재이면 마이페이지로 이동
                return "/redirect:/myPage/default";
            } else {
                Map<Object, Object> authorMap = new HashMap<>();
                authorMap = myPageService.findAuthorById(authorId);
                model.addAttribute("authorMap", authorMap);
                return "화면 구현안됨";
            }
        } else {
            msg = "회원만 보기가 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }

    /**
     * 작가 신청 하기 (마이페이지에서)
     */
    @GetMapping("/list/myPage/authorApplication")
    public String authorInfo(Model model, HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            Long sessionMemberId = member.getId();
            int result = myPageService.applicationInfo(sessionMemberId);

            if (result == 1) msg = "작가 신청이 완료되었습니다.";
            else if (result == -1) msg = "3개 이상 출간하셔야 합니다.";
            else if (result == 0) msg = "오류가 발생했습니다. 다시시도해주세요.";

            model.addAttribute("msg", msg);
            return "/redirect:/myPage/default";
        } else {
            msg = "회원만 신청이 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }
}
