package com.betterus.domain.mypage.repository;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.mypage.domain.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MyPageRepository extends JpaRepository<MyPage, Long> {

    @Query("select mp from MyPage mp")
    Optional<MyPage> findByMemberId(Long id);

//    @Query("select m,count(g)  from Member m fetch join m.gudoks g where id = :id")
//    Member findMemberAndGudokByMemberId(Long id);

}
