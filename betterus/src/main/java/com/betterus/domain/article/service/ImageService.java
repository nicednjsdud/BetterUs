package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Image;
import com.betterus.domain.article.dto.ImageDto;

import java.util.List;

public interface ImageService {
    List<Image> findAllByArticle(Long articleId);

    Image findByImageId(Long id);

    ImageDto findByArticleId(Long articleId);


    Image finddbImgByArticleId(Long id);

    void deleteImage(Image dbImg);
}
