package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.dto.ArticleForm;
import com.betterus.domain.member.domain.Member;
import com.betterus.model.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {

    int saveArticle(ArticleForm articleForm, Member member);

    Article findArticle(Long articleId);

    int updateArticle(Long articleId, ArticleForm articleForm);

    int deleteArticle(Long articleId);

    Page<ArticleDto> findArticleList(Pageable pageable);

    Page<ArticleDto> findSearchArticleList(String keyword, Pageable pageable);
}
