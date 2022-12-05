package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Image;
import com.betterus.domain.article.dto.ImageDto;
import com.betterus.domain.article.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final ImageRepository imageRepository;

    @Override
    public List<Image> findAllByArticle(Long articleId) {
        return imageRepository.findAllByArticle(articleId);
    }

    @Override
    public void deleteImage(Long id) {
       imageRepository.deleteImageById(id);
    }

    @Override
    public Image findByImageId(Long id) {
        return imageRepository.findByImageId(id);
    }

//    @Override
//    public List<ImageDto> findByArticleId(Long articleId) {
//        List<Image> imageList = imageRepository.findAllByArticle(articleId);
//        return imageList.stream().map(ImageDto::new).collect(Collectors.toList());
//    }
}