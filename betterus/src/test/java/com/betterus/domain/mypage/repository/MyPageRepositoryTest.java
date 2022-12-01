package com.betterus.domain.mypage.repository;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.domain.mypage.domain.MyPage;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MyPageRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MyPageRepository myPageRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Test
    @DisplayName("작가 아이디로 멤버 찾기")
    void findAuthorByAuthorId() {

        // given
        Member member = new Member("MemberA", "123123", "test@gmail.com", Grade.USER);
        Member saveMember = memberRepository.save(member);
        Long authorId = saveMember.getId();
        MyPage myPage = new MyPage(member);
        myPageRepository.save(myPage);

        // when
        Optional<MyPage> findMyPage = myPageRepository.findByMemberId(member.getId());
        MyPage findMyPageImpl = null;
        if (findMyPage.isPresent()) {
            findMyPageImpl = findMyPage.get();
        }

        // then
        assertThat(findMyPageImpl.getMember().getId()).isEqualTo(authorId);
    }

    @Test
    @DisplayName("멤버 아이디로 글 및 작가정보 가져오기")
    void findAuthorByMemberId() {
        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.AUTHOR);
        Member saveMember = memberRepository.save(member);
        MyPage findMypage = myPageRepository.save(new MyPage(member));
        Long authorId = saveMember.getId();

        for (int i = 0; i < 13; i++) {
            articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.APPROVAL, saveMember,findMypage));
        }
        em.flush();
        em.clear();

        // when
        Optional<MyPage> findMyPage = myPageRepository.findByMemberId(authorId);
        if (findMyPage.isPresent()) {
            MyPage myPage = findMyPage.get();
            List<Article> articleList = myPage.getArticleList();
            // then
            assertThat(articleList.size()).isEqualTo(13);
            assertThat(myPage.getMember().getId()).isEqualTo(authorId);
        }

    }
}