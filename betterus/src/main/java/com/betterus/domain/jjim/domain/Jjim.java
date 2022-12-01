/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 : 2022 - 11 - 30
 * 기능 : Jjim 테이블 엔티티
 */
package com.betterus.domain.jjim.domain;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.member.domain.Member;
import com.betterus.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Jjim extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "jjimId")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "articleId")
    private Article article;

    /**
     * 찜추가 용 폼
     */
    public Jjim(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
