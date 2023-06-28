package com.betterus.domain.article.repository;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.domain.QArticle;
import com.betterus.domain.article.domain.QImage;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.dto.QArticleDto;
import com.betterus.domain.member.domain.QMember;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.betterus.domain.article.domain.QArticle.*;
import static com.betterus.domain.article.domain.QImage.*;
import static com.betterus.domain.member.domain.QMember.*;
import static org.springframework.util.StringUtils.hasText;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ArticleDto> recommandList() {
        List<ArticleDto> articleList = queryFactory
                .select(
                        new QArticleDto(
                                article.id.as("articleId"),
                                article.title.as("title"),
                                article.subTitle.as("subTitle"),
                                article.contents.as("contents"),
                                article.status.as("status"),
                                article.reviewCount.as("reviewCount"),
                                article.jjimCount.as("jjimCount"),
                                image.fullPath.as("fullPath"),
                                member.nickName.as("nickName")
                        )
                )
                .from(article)
                .leftJoin(article.image,image)
                .leftJoin(article.member, member)
                .where(
                        member.grade.eq(Grade.AUTHOR),
                        article.status.eq(ArticleStatus.APPROVAL)
                )
                .limit(13)
                .orderBy(article.jjimCount.desc())
                .fetch();

        return articleList;
    }

    @Override
    public List<ArticleDto> newList() {
        List<ArticleDto> newList = queryFactory
                .select(
                        new QArticleDto(
                                article.id.as("articleId"),
                                article.title.as("title"),
                                article.subTitle.as("subTitle"),
                                article.contents.as("contents"),
                                article.status.as("status"),
                                article.reviewCount.as("reviewCount"),
                                article.jjimCount.as("jjimCount"),
                                image.fullPath.as("fullPath"),
                                member.nickName.as("nickName")
                        )
                )
                .from(article)
                .leftJoin(image.article, article)
                .leftJoin(article.member.articles, article)
                .where(
                        member.grade.eq(Grade.AUTHOR),
                        article.status.eq(ArticleStatus.APPROVAL)
                )
                .offset(30)
                .orderBy(article.createDate.desc())
                .fetch();

        return newList;
    }

}
