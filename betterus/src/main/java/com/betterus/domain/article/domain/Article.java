/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 : 2022 - 11 - 09
 * 기능 : Article 테이블 엔티티
 */
package com.betterus.domain.article.domain;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.articleseries.domain.ArticleSeries;
import com.betterus.domain.mypage.domain.MyPage;
import com.betterus.model.ArticleStatus;
import com.betterus.model.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

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

    @Column(name = "reviewCount")
    private Long reviewCount;

    @Column(name = "jjimCount")
    private Long jjimCount;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "articleSeriesId")
    private ArticleSeries articleSeries;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "myPageId")
    private MyPage myPage;

    /**
     *  리뷰count 와 찜count 초기 0 설정
     */
    @PrePersist
    public void prePersist(){
        if(this.reviewCount == null && this.jjimCount == null){
            this.reviewCount = 0L;
            this.jjimCount = 0L;
        }
    }

    public Article(String title, String subTitle, String contents, ArticleStatus status, Member member) {
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.status = status;
        this.member = member;
    }

    /**
     * 마이페이지 저장 및 article 저장
     */
    public Article(String title, String subTitle, String contents, ArticleStatus status, Member member,MyPage myPage) {
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.status = status;
        this.member = member;
        this.myPage = myPage;
    }

    /**
     * article 수정
     */
    public void changeArticle(String title,String subTitle,String contents){
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
    }

    /**
     *  리뷰 카운트 증가 or 삭제
     */
    public void changeReviewCount(Long reviewCount,String msg){
        if(msg == "리뷰추가") this.reviewCount += reviewCount;
        else if(msg == "리뷰삭제"){
            if(this.reviewCount !=0L)this.reviewCount -= reviewCount;
            else this.reviewCount = 0L;
        }
    }

    /**
     *  찜 카운트 증가 or 삭제
     */
    public void changeJjimCount(Long jjimCount,String msg){
        if(msg == "찜추가") this.jjimCount += jjimCount;
        else if(msg == "찜삭제"){
            if(this.jjimCount !=0L)this.jjimCount -= jjimCount;
            else this.jjimCount = 0L;
        }
    }

    /**
     *  article 상태 (취소, 승인, 대기중)
     */
    public void changeArticleStatus(String msg){
        if(msg =="작가신청") this.status = ArticleStatus.WAIT;
    }



}
