package com.betterus.domain.article.repository;

import com.betterus.domain.article.dto.ArticleDto;
import java.util.List;


public interface ArticleRepositoryCustom {

    List<ArticleDto> recommandList();

    List<ArticleDto> gudokList();
}
