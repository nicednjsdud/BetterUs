/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 29
 * 수정 일자 :
 * 기능 : Member 테이블 엔티티
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class JjimController {

    private final JjimService jjimService;


    /**
     * ajax 요청으로 들어온 gudok 추가
     */
    @GetMapping("/addJjim/{articleId}")
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

//    @GetMapping("/deleteGudok/{authorId}")
//    public String deleteGudok(@PathVariable("authorId") Long authorId, Model model, HttpServletRequest request) {
//        String msg = "";
//        HttpSession session = request.getSession();
//        Member member = (Member) session.getAttribute("member");
//        if (member != null) {
//            int result = gudokService.deleteGudok(member, authorId);
//            // 작가 상세정보 페이지
//            if (result == 1) msg = "구독 삭제가 완료되었습니다.";
//            else if(result == 2) msg = "이미 구독 삭제가 완료되었습니다.";
//            else msg = "오류가 발생하였습니다.";
//
//            model.addAttribute("msg", msg);
//            return "redirect: 화면 구현 x";
//        } else {
//            msg = "회원만 삭제가 가능합니다.";
//            model.addAttribute("msg", msg);
//            return "redirect:/";
//        }
//    }
}
