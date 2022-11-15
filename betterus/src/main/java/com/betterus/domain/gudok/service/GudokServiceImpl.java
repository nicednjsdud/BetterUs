package com.betterus.domain.gudok.service;

import com.betterus.domain.gudok.repository.GudokRepository;
import com.betterus.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GudokServiceImpl implements GudokService{

    private final GudokRepository gudokRepository;

    @Override
    @Transactional
    public String addGudok(Member member, Long articleId) {
        int result = gudokRepository.findByMemberId(member.getId());

        // 중복 추가 체크
        return "hello";
    }
}
