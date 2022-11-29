package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.dto.ArticleForm;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.mypage.domain.MyPage;
import com.betterus.domain.mypage.repository.MyPageRepository;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final MyPageRepository myPageRepository;

    /**
     * 게시글 저장
     */
    @Override
    @Transactional
    public int saveArticle(ArticleForm articleForm, Member member) {
        Article savedArticle = null;
        Optional<MyPage> memberMyPage = myPageRepository.findByMemberId(member.getId());
        if (memberMyPage.isPresent()) {
            MyPage myPage = memberMyPage.get();
            if (member.getGrade().equals(Grade.USER)) {
                Article article = new Article(articleForm.getTitle(), articleForm.getSubTitle(), articleForm.getContents(), ArticleStatus.WAIT, member, myPage);
                savedArticle = articleRepository.save(article);
            } else if (member.getGrade().equals(Grade.AUTHOR)) {
                Article article = new Article(articleForm.getTitle(), articleForm.getSubTitle(), articleForm.getContents(), ArticleStatus.APPROVAL, member, myPage);
                savedArticle = articleRepository.save(article);
            }
        }

        if (savedArticle != null) {
            return 1;
        } else return 0;
    }

    @Override
    public Article findArticle(Long articleId) {
        return articleRepository.findArticleById(articleId);
    }

    @Override
    @Transactional
    public int updateArticle(Long articleId, ArticleForm articleForm) {
        Article findArticle = articleRepository.findArticleById(articleId);
        Article checkArticle = findArticle;

        findArticle.changeArticle(articleForm.getTitle(), articleForm.getSubTitle(), articleForm.getContents());

        Article updateArticle = articleRepository.findArticleById(articleId);
        if (updateArticle.getTitle().equals(checkArticle.getTitle())) return 1;
        else return 0;
    }

    @Override
    @Transactional
    public int deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
        Article deleteArticle = articleRepository.findArticleById(articleId);
        if (deleteArticle == null) return 1;
        else return 0;
    }

    @Override
    public Page<ArticleDto> findArticleList(Pageable pageable) {
        Page<Article> findArticles = articleRepository.findByArticleStatus(ArticleStatus.APPROVAL, pageable);
        Page<ArticleDto> articleDtos = findArticles.map(article ->
                new ArticleDto(article.getId(), article.getTitle(), article.getSubTitle(), article.getContents(), article.getStatus(), article.getReviewCount(), article.getJjimCount()));
        return articleDtos;
    }

    @Override
    public Page<ArticleDto> findSearchArticleList(String keyword, Pageable pageable) {
        Page<Article> findArticles = articleRepository.findSearchListByTitleContaining(ArticleStatus.APPROVAL, keyword, pageable);
        Page<ArticleDto> articleDtos = findArticles.map(article ->
                new ArticleDto(article.getId(), article.getTitle(), article.getSubTitle(), article.getContents(), article.getStatus(), article.getReviewCount(), article.getJjimCount()));
        return articleDtos;
    }
}
