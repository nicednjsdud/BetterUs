/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 :
 * 기능 : Article 테이블 엔티티
 */
package com.betterus.domain.article.domain;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.articleseries.domain.ArticleSeries;
import com.betterus.model.ArticleStatus;
import com.betterus.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "articleId")
    private Long id;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "subTitle", length = 200)
    private String subTitle;

    @Column(name = "contents", length = 2000)
    private String contents;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "articleSeriesId")
    private ArticleSeries articleSeries;

    public Article(String title, String subTitle, String contents, ArticleStatus status, Member member) {
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.status = status;
        this.member = member;
    }
}
