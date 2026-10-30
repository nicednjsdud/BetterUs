package com.betterus.domain.main.service;

import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.service.ArticleService;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final ArticleService articleService;

    private final MemberService memberService;

    @Override
    public Map<String, Object> mainPageList() {
        Map<String, Object> mainList = new HashMap<>();
        List<ArticleDto> articleList = articleService.findArticleReCommandList();
        List<ArticleDto> gudokList = articleService.gudokList();
        mainList.put("articleList", articleList);
        mainList.put("gudokList", gudokList);
        // 추천 작가 — 최근 승인 순 상위 4명
        List<MemberDto> authorList = memberService
                .findAuthorByGrade(PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "id")))
                .getContent();
        mainList.put("authorList", authorList);
        return mainList;
    }
}
