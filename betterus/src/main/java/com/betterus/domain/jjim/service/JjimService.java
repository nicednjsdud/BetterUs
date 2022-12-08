package com.betterus.domain.jjim.service;

import com.betterus.domain.member.domain.Member;

public interface JjimService {
    int addJjim(Long id, Long articleId);

    int deleteJjim(Long id, Long articleId);

    Boolean findJjimTure(Long articleId, Long sessionMemberId);
}
