package com.betterus.domain.article.service;

import com.betterus.domain.article.dto.ArticleForm;
import com.betterus.domain.member.domain.Member;
import com.betterus.model.Grade;

public interface ArticleService {

    int saveArticle(ArticleForm articleForm, Member member);
}
