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
        List<ArticleDto> articleList = articleService.findArticleReCommandList();
        return null;
    }
}
