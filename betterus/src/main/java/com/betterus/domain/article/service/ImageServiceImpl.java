package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Image;
import com.betterus.domain.article.dto.ImageDto;
import com.betterus.domain.article.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService{

    private final ImageRepository imageRepository;

    @Override
    public List<Image> findAllByArticle(Long articleId) {
        return imageRepository.findAllByArticle(articleId);
    }

    @Override
    public Image findByImageId(Long id) {
        return imageRepository.findByImageId(id);
    }

    @Override
    public ImageDto findByArticleId(Long articleId) {
        Image findImage = imageRepository.findByArticleId(articleId);
        ImageDto image = new ImageDto(findImage.getOrigFileName(),findImage.getFullPath(),findImage.getFileSize());
        return image;
    }
    @Override
    public Image finddbImgByArticleId(Long id) {
        Image findImage = imageRepository.findByArticleId(id);
        return findImage;
    }

    @Override
    @Transactional
    public void deleteImage(Image dbImg) {
        imageRepository.delete(dbImg);
    }
}
