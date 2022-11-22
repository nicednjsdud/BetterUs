package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        ArticleForm articleForm = new ArticleForm("Test1","test1","테스트");
        Optional<Member> findMember1 = memberRepository.findById(1L);
        Optional<Member> findMember2 = memberRepository.findById(2L);
        if(findMember1.isPresent()) {
            Member findMemberUser = findMember1.get();
            articleService.saveArticle(articleForm, findMemberUser);
            List<Article> findMembers1 = articleRepository.findByMemberId(1L);
            Article articleUser = findMembers1.get(0);

            // then
            assertThat(articleUser.getStatus()).isEqualTo(ArticleStatus.WAIT);
        }

        else if(findMember2.isPresent()) {
            Member findMemberAuthor = findMember2.get();
            articleService.saveArticle(articleForm,findMemberAuthor);
            List<Article> findMembers2 = articleRepository.findByMemberId(2L);
            Article articleAuthor = findMembers2.get(0);

            // then
            assertThat(articleAuthor.getStatus()).isEqualTo(ArticleStatus.APPROVAL);
        }
    }

    @Test
    @DisplayName("articleId로 Article 찾기")
    public void findByArticleId(){
        Optional<Member> findMembers = memberRepository.findById(1L);
        if(findMembers.isPresent()) {
            Member findMember = findMembers.get();
            Article article1 = new Article("Test1", "test1", "테스트", ArticleStatus.WAIT,findMember);
            articleRepository.save(article1);

            em.flush();
            em.clear();

            Article findArticle = articleService.findArticle(3L);
            assertThat(findArticle.getTitle()).isEqualTo("Test1");
        }
    }

    @Test
    @DisplayName("article 수정 확인")
    public void updateArticle(){
        Optional<Member> findMembers = memberRepository.findById(1L);
        if(findMembers.isPresent()) {
            //given
            Member findMember = findMembers.get();
            Article article1 = new Article("Test1", "test1", "테스트", ArticleStatus.WAIT, findMember);
            articleRepository.save(article1);

            em.flush();
            em.clear();

            // when
            ArticleForm form = new ArticleForm("Test2","test2","테스트2");
            articleService.updateArticle(3L,form);
            Article findArticle = articleService.findArticle(3L);
            // then
            assertThat(findArticle.getTitle()).isEqualTo("Test2");
            assertThat(findArticle.getSubTitle()).isEqualTo("test2");
            assertThat(findArticle.getContents()).isEqualTo("테스트2");
        }

    }
    @Test
    @DisplayName("article 삭제 확인")
    public void deleteArticle(){
        Optional<Member> findMembers = memberRepository.findById(1L);
        if(findMembers.isPresent()) {
            //given
            Member findMember = findMembers.get();
            Article article1 = new Article("Test1", "test1", "테스트", ArticleStatus.WAIT, findMember);
            articleRepository.save(article1);

            em.flush();
            em.clear();

            // when
            int result = articleService.deleteArticle(3L);

            // then
            assertThat(result).isEqualTo(1);
        }

    }

    @Test
    @DisplayName("article list 10개씩 불러오기")
    public void articleListPaging(){
        Optional<Member> findMembers = memberRepository.findById(1L);
        if(findMembers.isPresent()) {
            //given
            Member findMember = findMembers.get();
            for (int i = 0; i < 13; i++) {
                articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.APPROVAL, findMember));
            }
            em.flush();
            em.clear();

            // when
            PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.DEFAULT_DIRECTION,"createDate"));
            Page<ArticleDto> findArticles = articleService.findArticleList(pageRequest);

            // then
            assertThat(findArticles.getSize()).isEqualTo(10);
            assertThat(findArticles.getTotalPages()).isEqualTo(2);
            assertThat(findArticles.getTotalElements()).isEqualTo(13);
            assertThat(findArticles.getNumber()).isEqualTo(0);
            assertThat(findArticles.isFirst()).isTrue();
            assertThat(findArticles.hasNext()).isTrue();
        }
    }
}