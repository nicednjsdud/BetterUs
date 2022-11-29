package com.betterus.domain.mypage.service;

import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface MyPageService {
    Map<Object, Object> findMyPageDefault(Member member);

    Map<Object, Object> findAuthorById(Long authorId);

    int applicationInfo(Long sessionMemberId);

    Page<MemberDto> findAdminPageDefault(Pageable pageable);

    Page<ArticleDto> findAdminPageConfirmArticle(Pageable pageable);

    Page<MemberDto> findSearchMemberList(String keyword, Pageable pageable);

    ArticleDto articleConfirmCheck(Long articleId);

    int authorPass(Long memberId);
}
