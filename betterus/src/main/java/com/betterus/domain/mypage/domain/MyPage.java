/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 04
 * 수정 일자 : 2022 = 11 = 22
 * 기능 : 마이페이지 엔티티
 */

package com.betterus.domain.mypage.domain;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyPage {

    @Id
    @GeneratedValue
    @Column(name = "myPageId")
    private Long id;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "myPage")
    private List<Article> articleList = new ArrayList<>();

    /**
     * 회원 가입 초기 마이페이지 생성
     */
    public MyPage(Member member) {
        this.member = member;
    }

    public MyPage(Member member, List<Article> articleList) {
        this.member = member;
        this.articleList = articleList;
    }

}
