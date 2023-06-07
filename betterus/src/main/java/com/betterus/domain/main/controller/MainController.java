/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 :
 * 기능 : MainController 메인화면
 */


package com.betterus.domain.main.controller;

import com.betterus.domain.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @RequestMapping("/")
    public String main(Model model){
        Map<String,Object> mainPageList = mainService.mainPageList();
        return "main/main";
    }

}
