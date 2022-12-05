/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 04
 * 수정 일자 : 2022 - 11 - 09
 * 기능 : article repository
 */


package com.betterus.domain.article.repository;

import com.betterus.domain.article.domain.Article;
import com.betterus.model.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findSearchListByTitleContaining(String keyword, Pageable pageable);

    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);


    List<Article> findByMemberId(Long id);

    Article findArticleById(Long id);

    Page<Article> findSearchListByStatusAndTitleContaining(ArticleStatus status, String title, Pageable pageable);

    Page<Article> findConfirmArticleByStatus(ArticleStatus status, Pageable pageable);

    List<Article> findByMemberId(Sort sort, Long id);
}
