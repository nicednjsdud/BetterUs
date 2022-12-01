package com.betterus.domain.article.repository;

import com.betterus.domain.article.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByArticle(Long articleId);

    void deleteImage(Long id);

    Image findByImageId(Long id);
}
