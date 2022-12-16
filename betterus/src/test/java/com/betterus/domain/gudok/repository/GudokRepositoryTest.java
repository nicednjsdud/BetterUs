package com.betterus.domain.gudok.repository;

import com.betterus.domain.gudok.domain.Gudok;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GudokRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    GudokRepository gudokRepository;
    
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("구독 추가 및 찾기")
    void gudokAddCheck(){
        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member member2 = new Member("Author", "12312323", "nicednjsdud23@gmail.com", Grade.ADMIN);
        memberRepository.save(member);

        memberRepository.save(member2);

        // when
        gudokRepository.save(new Gudok(member2.getId(),member));
        Gudok gudokTure = gudokRepository.findGudokTure(member2.getId(),member.getId());

        // then
        assertThat(gudokTure.getAuthorId()).isEqualTo(member2.getId());
        assertThat(gudokTure.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("구독 삭제")
    void gudokDelete(){
        // given
        Member member = new Member("MemberA", "123123", "nicednjsdud@gmail.com", Grade.USER);
        Member member2 = new Member("Author", "12312323", "nicednjsdud23@gmail.com", Grade.AUTHOR);
        memberRepository.save(member);
        memberRepository.save(member2);
        Long memberId = member.getId();
        Long member2Id = member2.getId();

        // when
        Gudok gudok = new Gudok(member2.getId(),member);
        gudokRepository.save(gudok);
        em.flush();
        em.clear();
        gudokRepository.delete(gudok);
        Gudok gudokTure = gudokRepository.findGudokTure(member2Id,memberId);

        // then
        assertThat(gudokTure).isNull();
    }
}