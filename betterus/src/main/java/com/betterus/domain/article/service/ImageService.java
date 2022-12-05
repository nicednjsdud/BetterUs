package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Image;
import com.betterus.domain.article.dto.ImageDto;

import java.util.List;

public interface ImageService {
    List<Image> findAllByArticle(Long articleId);

    void deleteImage(Long id);

    Image findByImageId(Long id);


//    List<ImageDto> findByArticleId(Long articleId);
}
