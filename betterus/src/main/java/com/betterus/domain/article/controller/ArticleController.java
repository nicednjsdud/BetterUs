/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 06
 * 수정 일자 :
 * 기능 : Article Controller
 */

package com.betterus.domain.article.controller;

import com.betterus.domain.article.dto.ArticleForm;
import com.betterus.domain.article.service.ArticleService;
import com.betterus.domain.member.domain.Member;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @RequestMapping("/list")
    public String articleListBasic(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {


        }

        return null;
    }

    @GetMapping("/write")
    public String writeArticleForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            model.addAttribute("articleForm", new ArticleForm());
            return "writing/writing";
        } else {
            String msg = "회원만 글쓰기가 가능합니다.";
            model.addAttribute("msg",msg);
            return "redirect:/";
        }

    }

    @PostMapping("/write")
    public String writeArticle(ArticleForm articleForm, Model model, HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("member");
        if (member != null) {
            int result = articleService.saveArticle(articleForm,member);
            if (result == 1) {
                msg = "글쓰기가 완료되었습니다.";
                model.addAttribute("msg",msg);
                return "myPage/mypage(info)";
            }
            else{
                msg ="오류가 발생했습니다. 다시 시도해주세요.";
                model.addAttribute("msg",msg);
                return "writing/writing";
            }
        } else {
            msg = "회원만 글쓰기가 가능합니다.";
            model.addAttribute("msg",msg);
            return "redirect:/";
        }
    }

}