package com.betterus.domain.gudok.service;

import com.betterus.domain.member.domain.Member;

public interface GudokService {
    String addGudok(Member member, Long articleId);

}
