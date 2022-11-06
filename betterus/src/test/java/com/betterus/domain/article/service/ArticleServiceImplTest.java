package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleForm;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArticleServiceImplTest {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ArticleService articleService;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    @DisplayName("회원가입")
    public void join() {
        Member User = new Member("User", "123123", "nicednjsdud12@gmail.com", Grade.USER);
        Member Author = new Member("Author", "123123", "nicednjsdud123@gmail.com", Grade.AUTHOR);
        memberRepository.save(User);
        memberRepository.save(Author);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("게시글 저장 테스트")
    public void articleSave(){
        Optional<Member> findMember1 = memberRepository.findById(1L);
        Member findMemberUser = findMember1.get();
        Optional<Member> findMember2 = memberRepository.findById(2L);
        Member findMemberAuthor = findMember2.get();
        ArticleForm articleForm = new ArticleForm("Test1","test1","테스트");

        articleService.saveArticle(articleForm, findMemberUser);
        articleService.saveArticle(articleForm,findMemberAuthor);

        List<Article> findMembers1 = articleRepository.findByMemberId(1L);
        Article articleUser = findMembers1.get(0);

        List<Article> findMembers2 = articleRepository.findByMemberId(2L);
        Article articleAuthor = findMembers2.get(0);

        assertThat(articleUser.getStatus()).isEqualTo(ArticleStatus.WAIT);
        assertThat(articleAuthor.getStatus()).isEqualTo(ArticleStatus.APPROVAL);
    }
}