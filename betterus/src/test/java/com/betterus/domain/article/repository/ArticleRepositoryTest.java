package com.betterus.domain.article.repository;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.domain.Image;
import com.betterus.domain.article.dto.ArticleDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
class ArticleRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ImageRepository imageRepository;


    @Test
    @DisplayName("게시글 저장")
    public void saveByStatus() {
        Member User = new Member("User", "123123", "nicednjsdud12@gmail.com", Grade.USER);
        Member Author = new Member("Author", "123123", "nicednjsdud123@gmail.com", Grade.AUTHOR);
        memberRepository.save(User);
        memberRepository.save(Author);
        Optional<Member> findMembers = memberRepository.findById(1L);
        if (findMembers.isPresent()) {
            Member findMember = findMembers.get();
            if (findMember.getGrade().equals(Grade.USER)) {
                Article article1 = new Article("Test1", "test1", "테스트", ArticleStatus.WAIT, findMember);
                articleRepository.save(article1);
            } else if (findMember.getGrade().equals(Grade.AUTHOR)) {
                Article article1 = new Article("Test2", "test2", "테스트", ArticleStatus.APPROVAL, findMember);
                articleRepository.save(article1);
            }

            List<Article> findArticles = articleRepository.findByMemberId(1L);
            Article findArticle = findArticles.get(0);

            assertThat(findArticle.getStatus()).isEqualTo(ArticleStatus.WAIT);
            assertThat(findArticle.getTitle()).isEqualTo("Test1");
        }
    }

    @Test
    @DisplayName("게시글 Id로 한개 불러오기")
    public void findByArticleId() {
        Member User = new Member("User", "123123", "nicednjsdud12@gmail.com", Grade.USER);
        Member Author = new Member("Author", "123123", "nicednjsdud123@gmail.com", Grade.AUTHOR);
        memberRepository.save(User);
        memberRepository.save(Author);
        Optional<Member> findMembers = memberRepository.findById(1L);
        if (findMembers.isPresent()) {
            Member findMember = findMembers.get();
            Article article1 = new Article("Test1", "test1", "테스트", ArticleStatus.WAIT, findMember);
            articleRepository.save(article1);

            em.flush();
            em.clear();

            Article findArticle = articleRepository.findArticleById(3L);
            assertThat(findArticle.getTitle()).isEqualTo("Test1");
        }
    }

    @Test
    @DisplayName("기본 article 리스트 불러오기 - 10개씩")
    public void findArticlePaging() {
        Member User = new Member("User", "123123", "nicednjsdud12@gmail.com", Grade.USER);
        Member Author = new Member("Author", "123123", "nicednjsdud123@gmail.com", Grade.AUTHOR);
        memberRepository.save(User);
        memberRepository.save(Author);
        Optional<Member> findMembers = memberRepository.findById(1L);
        if (findMembers.isPresent()) {
            Member findMember = findMembers.get();
            for (int i = 0; i < 13; i++) {
                articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.APPROVAL, findMember));
            }
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.DEFAULT_DIRECTION, "createDate"));
            Page<Article> findArticles = articleRepository.findByStatus(ArticleStatus.APPROVAL, pageRequest);

            assertThat(findArticles.getSize()).isEqualTo(10);
            assertThat(findArticles.getTotalPages()).isEqualTo(2);
            assertThat(findArticles.getTotalElements()).isEqualTo(13);
            assertThat(findArticles.getNumber()).isEqualTo(0);
            assertThat(findArticles.isFirst()).isTrue();
            assertThat(findArticles.hasNext()).isTrue();
        }

    }

    @Test
    @DisplayName("작가신청 article 리스트 불러오기 - 10개씩")
    public void findConfirmArticlePaging() {
        Member User = new Member("User", "123123", "nicednjsdud12@gmail.com", Grade.USER);
        memberRepository.save(User);
        Optional<Member> findMembers = memberRepository.findById(1L);
        if (findMembers.isPresent()) {
            Member findMember = findMembers.get();
            for (int i = 0; i < 13; i++) {
                articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.WAIT, findMember));
            }
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.DEFAULT_DIRECTION, "member.authorConfirmDate"));
            Page<Article> findArticles = articleRepository.findConfirmArticleByStatus(ArticleStatus.WAIT, pageRequest);

            assertThat(findArticles.getSize()).isEqualTo(10);
            assertThat(findArticles.getTotalPages()).isEqualTo(2);
            assertThat(findArticles.getTotalElements()).isEqualTo(13);
            assertThat(findArticles.getNumber()).isEqualTo(0);
            assertThat(findArticles.isFirst()).isTrue();
            assertThat(findArticles.hasNext()).isTrue();
        }
    }

    @Test
    @DisplayName("추천 글 불러오기")
    public void recommandList() {
        Member User = new Member("User", "123123", "nicednjsdud12@gmail.com", Grade.AUTHOR);
        Member savedMember = memberRepository.save(User);
        Optional<Member> findMembers = memberRepository.findById(savedMember.getId());
        if (findMembers.isPresent()) {
            Member findMember = findMembers.get();
            for (int i = 0; i < 13; i++) {
                Article savedArticle = articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.APPROVAL, findMember));
                Image savedImage = imageRepository.save(new Image("Image" + i, "Image" + i, 1L));
                savedImage.setArticle(savedArticle);
                for (int j = 0; j < i + 2; j++) {
                    savedArticle.changeJjimCount("찜추가");
                }
            }
        }
        List<ArticleDto> findArticles = articleRepository.recommandList();
        for (ArticleDto findArticle : findArticles) {
            System.out.println(findArticle.getId());
        }
        assertThat(findArticles.size()).isEqualTo(13);
        assertThat(findArticles.get(0).getTitle()).isEqualTo("Test12");
    }

    @Test
    @DisplayName("구독 순 글 불러오기")
    public void gudokList() {
        for (int k = 0; k < 13; k++) {
            Member User = new Member("User" + k, "123123", "nicednjsdud12@gmail.com", Grade.AUTHOR);
            Member savedUser = memberRepository.save(User);
            for(int i = 0; i<k+1;i++){
                savedUser.changeGudokCount((long)i,"구독추가");
            }

        }
        List<Member> findMembers = memberRepository.findAll();
        if (findMembers.size() != 0) {
            for (Member findMember : findMembers) {
                for (int i = 0; i < 4; i++) {
                    Article savedArticle = articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.APPROVAL, findMember));
                    Image savedImage = imageRepository.save(new Image("Image" + i, "Image" + i, 1L));
                    savedImage.setArticle(savedArticle);
                }
            }
        }

        List<ArticleDto> findArticles = articleRepository.gudokList();
        assertThat(findArticles.get(0).getNickName()).isEqualTo("User12");
        assertThat(findArticles.size()).isEqualTo(30);
    }
}