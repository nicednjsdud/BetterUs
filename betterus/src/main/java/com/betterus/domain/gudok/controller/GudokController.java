/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 18
 * 수정 일자 :
 * 기능 : gudok controller
 */


package com.betterus.domain.gudok.controller;

import com.betterus.domain.gudok.service.GudokService;
import com.betterus.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class GudokController {

    private final GudokService gudokService;


    /**
     * ajax 요청으로 들어온 gudok 추가
     */
    @GetMapping("gudok/{authorId}/addGudok")
    public String addGudok(@PathVariable("authorId") Long authorId, Model model, HttpServletRequest request) {

        String msg = "";
        HttpSession session = request.getSession();
        Object sessionMember = session.getAttribute("member");
        Long sessionMemberId = (Long) sessionMember;
        if (sessionMemberId != null) {
            int result = gudokService.addGudok(sessionMemberId, authorId);
            // 작가 상세정보 페이지
            if (result == 1) msg = "구독 추가가 완료되었습니다.";
            else msg = "이미 구독 추가하셨습니다.";

            model.addAttribute("msg", msg);

            String url = "list/authorPage/"+authorId+"/default";
            return "redirect:/"+url;
        } else {
            msg = "회원만 구독이 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }

    @GetMapping("gudok/{authorId}/deleteGudok")
    public String deleteGudok(@PathVariable("authorId") Long authorId, Model model, HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession();
        Object sessionMember = session.getAttribute("member");
        Long sessionMemberId = (Long) sessionMember;
        if (sessionMemberId != null) {
            int result = gudokService.deleteGudok(sessionMemberId, authorId);
            // 작가 상세정보 페이지
            if (result == 1) msg = "구독 취소가 완료되었습니다.";
            else if(result == 2) msg = "이미 구독 취소가 완료되었습니다.";
            else msg = "오류가 발생하였습니다.";

            model.addAttribute("msg", msg);
            String url = "list/authorPage/"+authorId+"/default";
            return "redirect:/"+url;
        } else {
            msg = "회원만 취소가 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }

}
