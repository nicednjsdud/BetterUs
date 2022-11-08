package com.betterus.domain.article.repository;

import com.betterus.domain.article.domain.Article;
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
class ArticleRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    MemberRepository memberRepository;

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
    @DisplayName("게시글 저장")
    public void saveByStatus() {
        Optional<Member> findMembers = memberRepository.findById(1L);
        Member findMember = findMembers.get();
        if (findMember.getGrade().equals(Grade.USER)) {
            Article article1 = new Article("Test1", "test1", "테스트", ArticleStatus.WAIT,findMember);
            articleRepository.save(article1);
        }
        else if(findMember.getGrade().equals(Grade.AUTHOR)){
            Article article1 = new Article("Test2", "test2", "테스트", ArticleStatus.APPROVAL,findMember);
            articleRepository.save(article1);
        }

        List<Article> findArticles = articleRepository.findByMemberId(1L);
        Article findArticle = findArticles.get(0);

        assertThat(findArticle.getStatus()).isEqualTo(ArticleStatus.WAIT);
        assertThat(findArticle.getTitle()).isEqualTo("Test1");
    }

    @Test
    @DisplayName("게시글 Id로 한개 불러오기")
    public void findByArticleId(){
        Optional<Member> findMembers = memberRepository.findById(1L);
        if(findMembers.isPresent()) {
            Member findMember = findMembers.get();
            Article article1 = new Article("Test1", "test1", "테스트", ArticleStatus.WAIT,findMember);
            articleRepository.save(article1);

            em.flush();
            em.clear();

            Article findArticle = articleRepository.findArticleById(3L);
            assertThat(findArticle.getTitle()).isEqualTo("Test1");
        }
    }

//    @Test
//    @DisplayName("기본 리스트 목록 불러오기 (페이징 처리)")
//    public void pagingFindAll() {
//        PageRequest pageRequest = PageRequest.of(0,3,Sort.by(Sort.Direction.DESC))
//    }

}