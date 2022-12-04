package com.betterus.domain.mypage.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MyPageServiceImplTest {


    @PersistenceContext
    EntityManager em;

    @Autowired
    MyPageService myPageService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MyPageRepository myPageRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Test
    @DisplayName("멤버 아이디로 회원 정보, articleList 가져오기")
    void findMyPageListByMemberId() {
        // given
        Member member = new Member("MemberB", "123123", "test545@gmail.com", Grade.USER);
        Member saveMember = memberRepository.save(member);
        MyPage findMypage = myPageRepository.save(new MyPage(member));
        Long authorId = saveMember.getId();

        for (int i = 0; i < 13; i++) {
            articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.APPROVAL, saveMember, findMypage));
        }
        em.flush();
        em.clear();

        // when
        Map<Object, Object> findMyPageList = myPageService.findAuthorById(authorId);
        List<ArticleDto> articleDtoList = (List<ArticleDto>) findMyPageList.get("articleDtoList");
        MemberDto memberDto = (MemberDto) findMyPageList.get("memberDto");
        // then
        assertThat(articleDtoList.size()).isEqualTo(13);
        assertThat(memberDto.getNickName()).isEqualTo("MemberA");
    }

    @Test
    @DisplayName("글상태 3개이상이면 대기중으로 변경하기")
    void changeArticleStatus() {
        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member saveMember = memberRepository.save(member);
        MyPage findMypage = myPageRepository.save(new MyPage(member));
        Long authorId = saveMember.getId();

        for (int i = 0; i < 13; i++) {
            articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.SAVE, saveMember, findMypage));
        }
        em.flush();
        em.clear();

        // when
        int result = myPageService.applicationInfo(authorId);
        Optional<MyPage> findMyPage = myPageRepository.findByMemberId(authorId);
        if (findMyPage.isPresent()) {
            MyPage myPage = findMyPage.get();
            Article article = myPage.getArticleList().get(1);

            // then
            assertThat(result).isEqualTo(1);
            assertThat(article.getStatus()).isEqualTo(ArticleStatus.WAIT);
        }

    }

    @Test
    @DisplayName("작가신청 리스트 페이지로 가져오기")
    void findArticleConfirmByArticleStatus() {
        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member saveMember = memberRepository.save(member);
        MyPage findMypage = myPageRepository.save(new MyPage(member));
        Long authorId = saveMember.getId();
        for (int i = 0; i < 13; i++) {
            articleRepository.save(new Article("Test" + i, "test" + i, "테스트" + i, ArticleStatus.WAIT, saveMember, findMypage));
        }
        em.flush();
        em.clear();

        // when
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.DEFAULT_DIRECTION,"member.authorConfirmDate"));
        Page<ArticleDto> articleList = myPageService.findAdminPageConfirmArticle(pageRequest);
        // then
        assertThat(articleList.getSize()).isEqualTo(10);
        assertThat(articleList.getTotalPages()).isEqualTo(2);
        assertThat(articleList.getTotalElements()).isEqualTo(13);
        assertThat(articleList.getNumber()).isEqualTo(0);
        assertThat(articleList.isFirst()).isTrue();
        assertThat(articleList.hasNext()).isTrue();
        assertThat(articleList.get().findFirst().get().getStatus()).isEqualTo(ArticleStatus.WAIT);
    }

    @Test
    @DisplayName("관리자 페이지 합격/불합격 article 한개 보이기용")
    void findArticle() {
        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member saveMember = memberRepository.save(member);
        MyPage findMypage = myPageRepository.save(new MyPage(member));
        Long authorId = saveMember.getId();
        Article saveArticle = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.WAIT, saveMember, findMypage));
        Long id = saveArticle.getId();
        em.flush();
        em.clear();

        // when
        Article findArticle = articleRepository.findArticleById(id);
        ArticleDto articleDto = myPageService.articleConfirmCheck(findArticle.getId());

        // then
        assertThat(articleDto.getNickName()).isEqualTo("MemberA");
        assertThat(articleDto.getContents()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("작가 신청 승인")
    void authorPass(){
        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member saveMember = memberRepository.save(member);
        MyPage findMypage = myPageRepository.save(new MyPage(member));
        Long authorId = saveMember.getId();
        Article saveArticle = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.WAIT, saveMember, findMypage));
        Article saveArticle1 = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.WAIT, saveMember, findMypage));
        Article saveArticle2 = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.WAIT, saveMember, findMypage));
        Long id = saveArticle.getId();
        em.flush();
        em.clear();

        // when
        int result = myPageService.authorPass(authorId);
        Optional<Member> findMember = memberRepository.findById(authorId);
        Article findArticle = articleRepository.findArticleById(id);
        if(findMember.isPresent()){
            Member member1 = findMember.get();
        // then
            assertThat(result).isEqualTo(1);
            assertThat(member1.getGrade()).isEqualTo(Grade.AUTHOR);
            assertThat(findArticle.getStatus()).isEqualTo(ArticleStatus.APPROVAL);
        }

    }

    @Test
    @DisplayName("작가 신청 불승인")
    void authorFail(){
        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member saveMember = memberRepository.save(member);
        MyPage findMypage = myPageRepository.save(new MyPage(member));
        Long authorId = saveMember.getId();
        Article saveArticle = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.WAIT, saveMember, findMypage));
        Article saveArticle1 = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.WAIT, saveMember, findMypage));
        Article saveArticle2 = articleRepository.save(new Article("Test", "test", "테스트", ArticleStatus.WAIT, saveMember, findMypage));
        Long id = saveArticle.getId();
        em.flush();
        em.clear();

        // when
        int result = myPageService.authorFail(authorId);
        Optional<Member> findMember = memberRepository.findById(authorId);
        Article findArticle = articleRepository.findArticleById(id);
        if(findMember.isPresent()){
            Member member1 = findMember.get();
            // then
            assertThat(result).isEqualTo(1);
            assertThat(member1.getGrade()).isEqualTo(Grade.USER);
            assertThat(findArticle.getStatus()).isEqualTo(ArticleStatus.CANCEL);
        }

    }
}