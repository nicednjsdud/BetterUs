package com.betterus.domain.gudok.service;

import com.betterus.domain.member.domain.Member;

public interface GudokService {
    int addGudok(Member member, Long articleId);

    int deleteGudok(Member member, Long authorId);

    boolean findGudokCheck(Long authorId, Long sessionMemberId);
}
