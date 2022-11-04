/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 :
 * 기능 : MemberController
 */


package com.betterus.domain.member.controller;


import com.betterus.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


}
