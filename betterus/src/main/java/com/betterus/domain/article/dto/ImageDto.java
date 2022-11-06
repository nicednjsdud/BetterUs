package com.betterus.domain.article.dto;

import com.betterus.domain.article.domain.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
public class ImageDto {

    private Long id;
    private String fileName;
    private String fullPath;
}
