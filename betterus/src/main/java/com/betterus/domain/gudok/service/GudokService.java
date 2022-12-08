package com.betterus.domain.gudok.service;

import com.betterus.domain.member.domain.Member;

public interface GudokService {
    int addGudok(Long memberId, Long articleId);

    int deleteGudok(Long memberId, Long authorId);

    boolean findGudokCheck(Long authorId, Long sessionMemberId);
}
