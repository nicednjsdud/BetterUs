/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 :
 * 기능 : 멤버 repository
 */

package com.betterus.domain.member.repository;

import com.betterus.domain.member.domain.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmailAndPassword(String email, String password);

    Member findByEmail(String email);

    Member findByNickName(String nickName);

}
