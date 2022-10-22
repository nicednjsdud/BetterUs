/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 :
 * 기능 : ArticleSeries 테이블 엔티티
 */
package com.betterus.domain.articleseries.domain;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.member.domain.Member;
import com.betterus.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleSeries extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "articleSeriesId")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "articleSeries")
    private List<Article> articleSeriesList = new ArrayList<>();
}
