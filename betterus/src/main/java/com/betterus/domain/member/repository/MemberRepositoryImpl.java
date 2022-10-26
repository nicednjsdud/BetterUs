/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 22
 * 수정 일자 :
 * 기능 : MemberRepository
 */

package com.betterus.domain.member.repository;

import com.betterus.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

}
