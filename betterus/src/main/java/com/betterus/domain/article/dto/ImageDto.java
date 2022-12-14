package com.betterus.domain.article.dto;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageDto {

    private Long id;
    private String origFileName;
    private String fullPath;
    private Long fileSize;

    @Builder
    public ImageDto(String origFileName, String fullPath, Long fileSize) {
        this.origFileName = origFileName;
        this.fullPath = fullPath;
        this.fileSize = fileSize;
    }

    public ImageDto(Image image) {
        this.id = image.getId();
    }
}
