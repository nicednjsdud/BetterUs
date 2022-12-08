package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.dto.ArticleForm;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {

    

    ArticleDto findArticle(Long articleId);

    int updateArticle(Long articleId, ArticleForm articleForm, List<MultipartFile> addFileList) throws Exception;

    int deleteArticle(Long articleId);

    Page<ArticleDto> findArticleList(Pageable pageable);

    Page<ArticleDto> findSearchArticleList(String keyword, Pageable pageable);

    int saveArticle(ArticleForm articleForm, Member member, List<MultipartFile> files) throws Exception;

    Article findAuthrByArticleId(Long articleId);

    ArticleDto findArticleByArticleId(Long articleId);
}
