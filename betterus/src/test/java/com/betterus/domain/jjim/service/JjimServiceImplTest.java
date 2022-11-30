package com.betterus.domain.jjim.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.jjim.domain.Jjim;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.domain.mypage.domain.MyPage;
import com.betterus.domain.mypage.repository.MyPageRepository;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JjimServiceImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JjimService jjimService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    MyPageRepository myPageRepository;

    @Test
    @DisplayName("찜추가 및 찜카운트 테스트")
    void addJjimAndAddJjimCount(){

        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.AUTHOR);
        Member member2 = new Member("MemberB","124123","nicednjsdud12@gmail.com",Grade.USER);
        Member saveMember = memberRepository.save(member);
        Member saveMember2 = memberRepository.save(member2);
        MyPage findMypage = myPageRepository.save(new MyPage(member));
        Long userId = saveMember2.getId();
        Article saveArticle = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.APPROVAL, saveMember, findMypage));
        Article saveArticle1 = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.APPROVAL, saveMember, findMypage));
        Article saveArticle2 = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.APPROVAL, saveMember, findMypage));
        Long id = saveArticle.getId();
        em.flush();
        em.clear();
        // when
        int result = jjimService.addJjim(saveMember2, id);
        Article findArticle = articleRepository.findArticleById(id);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(findArticle.getJjimCount()).isEqualTo(1L);
    }
}