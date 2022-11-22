package com.betterus.domain.member.repository;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("로그인 성공")
    public void login() {

        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findByEmailAndPassword("nicednjsdud@gmail.com", "123123");


        assertThat(findMember.getEmail()).isEqualTo(saveMember.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(saveMember.getPassword());
    }

    @Test
    @DisplayName("로그인 실패")
    public void loginFail() {

        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findByEmailAndPassword("nice@gmail.com", "123123");
        Member findMember2 = memberRepository.findByEmailAndPassword("nicednjsdud@gmail.com", "2345");

        assertThrows(NullPointerException.class, () -> findMember.getId());
        assertThrows(NullPointerException.class, () -> findMember2.getId());
    }

    @Test
    @DisplayName("멤버 저장하기")
    public void saveMember() {

        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Optional<Member> find = memberRepository.findById(saveMember.getId());
        Member findMember = find.get();

        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
    }

    @Test
    @DisplayName("멤버 이메일로 찾기")
    public void findMemberByEmail() {

        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findByEmail("nicednjsdud@gmail.com");

        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
    }

    @Test
    @DisplayName("멤버 이메일로 찾기 실패")
    public void findMemberByEmailFail() {

        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findByEmail("nicednjsdud@gmail23.com");

        assertThrows(NullPointerException.class, () -> findMember.getId());
    }

    @Test
    @DisplayName("멤버 이름으로 찾기")
    public void findMemberByNickname() {

        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findByNickName("MemberA");

        assertThat(findMember.getNickName()).isEqualTo(saveMember.getNickName());
    }

    @Test
    @DisplayName("멤버 이름으로 찾기 실패")
    public void findMemberByNicknameFail() {

        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findByNickName("MemberBBBBB");

        assertThrows(NullPointerException.class, () -> findMember.getId());
    }

    @Test
    @DisplayName("멤버 아이디로 조회하기")
    public void findMemberById() {

        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.ADMIN);
        Member saveMember = memberRepository.save(member);

        Optional<Member> find = memberRepository.findById(saveMember.getId());
        if(find.isPresent()){
            Member findMember = find.get();


            assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        }

    }

    @Test
    @DisplayName("작가 찾기 페이징")
    @Rollback(value = false)
    public void findAuthorByGrade(){
        for (int i = 0; i < 13; i++) {
            memberRepository.save(new Member("Member"+i, "123123"+i, "nicednjsdud@gmail.com"+i, Grade.AUTHOR));
        }


        Optional<Member> findMember = memberRepository.findById(3L);
        Member member = findMember.get();
        em.flush();
        em.clear();

        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.DEFAULT_DIRECTION,"gudok_count"));
        Page<MemberDto> findAuthors = memberRepository.findAuthorByGrade(Grade.AUTHOR,pageRequest);


        assertThat(findAuthors.getSize()).isEqualTo(10);
        assertThat(findAuthors.getTotalPages()).isEqualTo(2);
        assertThat(findAuthors.getTotalElements()).isEqualTo(13);
        assertThat(findAuthors.getNumber()).isEqualTo(0);
        assertThat(findAuthors.isFirst()).isTrue();
        assertThat(findAuthors.hasNext()).isTrue();


    }
}