package com.betterus.domain.main.service;

import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.*;
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService{

    private final ArticleService articleService;

    @Override
    public Map<String, Object> mainPageList() {
        Map<String,Object> mainList = new HashMap<>();
        List<ArticleDto> articleList = articleService.findArticleReCommandList();
        List<ArticleDto> gudokList = articleService.gudokList();
        mainList.put("articleList",articleList);
        mainList.put("gudokList",gudokList);
        return mainList;
    }
}
