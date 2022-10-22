/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 :
 * 기능 : Review 테이블 엔티티
 */

package com.betterus.domain.review.domain;

import com.betterus.domain.article.domain.Article;
import com.betterus.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "reviewId")
    private Long id;

    @Column(name = "contents", length = 2000)
    private String contents;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "articleId")
    private Article article;
}
