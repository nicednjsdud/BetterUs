package com.betterus.domain.member.repository;

import com.betterus.domain.member.domain.Member;
import org.springframework.dao.DataAccessException;

public interface MemberRepository {

    void save(Member member) throws DataAccessException;
}
