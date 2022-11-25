package com.betterus.domain.mypage.service;

import com.betterus.domain.member.domain.Member;

import java.util.Map;

public interface MyPageService {
    Map<Object, Object> findMyPageDefault(Member member);

    Map<Object, Object> findAuthorById(Long authorId);

    int applicationInfo(Long sessionMemberId);
}
