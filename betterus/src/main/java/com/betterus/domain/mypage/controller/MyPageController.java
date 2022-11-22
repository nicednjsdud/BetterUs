/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 04
 * 수정 일자 :
 * 기능 : 마이페이지 컨트롤러
 */

package com.betterus.domain.mypage.controller;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
            model.addAttribute("myPage",myPage);
            return "/myPage/myPage(info)";
        } else {
            String msg = "회원만 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }
}
