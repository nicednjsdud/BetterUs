/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 29
 * 수정 일자 : 2022 - 11 - 30
 * 기능 : Jjim Controller
 */

package com.betterus.domain.jjim.controller;

import com.betterus.domain.gudok.service.GudokService;
import com.betterus.domain.jjim.service.JjimService;
import com.betterus.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class JjimController {

    private final JjimService jjimService;


    /**
     * ajax 요청으로 들어온 찜 추가
     */
    @PostMapping("/addJjim/{articleId}")
    public String addGudok(@PathVariable("articleId") Long articleId, Model model, HttpServletRequest request) {

        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            int result = jjimService.addJjim(member, articleId);
            // 작가 상세정보 페이지
            if (result == 1) msg = "찜 추가가 완료되었습니다.";
            else msg = "이미 찜 추가하셨습니다.";
            model.addAttribute("msg", msg);
            return "article/a_article";
        } else {
            msg = "회원만 찜이 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }

    /**
     * ajax 요청으로 들어온 찜 삭제
     */
    @PostMapping("/deleteJjim/{articleId}")
    public String deleteJjim(@PathVariable("articleId") Long articleId, Model model, HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            int result = jjimService.deleteJjim(member, articleId);
            // 작가 상세정보 페이지
            if (result == 1) msg = "찜 삭제가 완료되었습니다.";
            else msg = "오류가 발생하였습니다.";
            model.addAttribute("msg", msg);
            return "redirect: 화면 구현 x";
        } else {
            msg = "회원만 삭제가 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }
}
