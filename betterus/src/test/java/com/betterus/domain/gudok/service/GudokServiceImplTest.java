package com.betterus.domain.gudok.service;

import com.betterus.domain.gudok.domain.Gudok;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.model.Grade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GudokServiceImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    GudokService gudokService;

    @Test
    @DisplayName("구독 추가")
    void addGudok() {

        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member member2 = new Member("Author", "12312323", "nicednjsdud23@gmail.com", Grade.AUTHOR);
        memberRepository.save(member);
        memberRepository.save(member2);

        // when
        int result = gudokService.addGudok(member, member2.getId());

        // then
        assertThat(result).isEqualTo(1);

    }

    @Test
    @DisplayName("구독 추가 실패")
    void addGudokFail() {

        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member member2 = new Member("Author", "12312323", "nicednjsdud23@gmail.com", Grade.AUTHOR);
        memberRepository.save(member);
        memberRepository.save(member2);

        // when
        int result = gudokService.addGudok(member, member2.getId());
        int result2 = gudokService.addGudok(member, member2.getId());

        // then
        assertThat(result2).isEqualTo(2);

    }
}