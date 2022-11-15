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
    @GetMapping("list/{articleId}/addGudok")
    public String addGudok(@PathVariable("articleId")Long articleId, Model model, HttpServletRequest request){

        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            String status = gudokService.addGudok(member,articleId);
        }
        else{
            msg = "회원만 구독이 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
        return null;
    }
}
