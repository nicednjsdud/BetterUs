package com.betterus.domain.jjim.service;

import com.betterus.domain.member.domain.Member;

public interface JjimService {
    int addJjim(Member member, Long articleId);

    int deleteJjim(Member member, Long articleId);
}
