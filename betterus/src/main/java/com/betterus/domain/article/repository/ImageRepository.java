package com.betterus.domain.article.repository;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByArticle(Long articleId);

    void deleteImageById(Long id);

    @Query("select i from Image i")
    Image findByImageId(Long imageId);

    Image findByArticleId(Long articleId);

    Image findByArticle(Article article);
}
