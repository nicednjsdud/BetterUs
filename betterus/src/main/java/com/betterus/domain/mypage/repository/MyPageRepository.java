package com.betterus.domain.mypage.repository;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.mypage.domain.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MyPageRepository extends JpaRepository<MyPage, Long> {

//    @Query("select m,count(g)  from Member m fetch join m.gudoks g where id = :id")
//    Member findMemberAndGudokByMemberId(Long id);

}
