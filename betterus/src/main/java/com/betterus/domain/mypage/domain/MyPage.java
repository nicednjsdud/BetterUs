package com.betterus.domain.mypage.domain;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
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


}
