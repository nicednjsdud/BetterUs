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
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Map;

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
    void findMyPageListByMemberId(){
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
        Map<Object, Object> findMyPageList = myPageService.findAuthorById(authorId);
        List<ArticleDto> articleDtoList = (List<ArticleDto>) findMyPageList.get("articleDtoList");
        MemberDto memberDto = (MemberDto) findMyPageList.get("memberDto");
        // then
        assertThat(articleDtoList.size()).isEqualTo(13);
        assertThat(memberDto.getNickName()).isEqualTo("MemberA");
    }
}