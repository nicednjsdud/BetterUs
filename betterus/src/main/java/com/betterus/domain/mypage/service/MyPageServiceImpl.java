package com.betterus.domain.mypage.service;

import com.betterus.domain.member.domain.Member;
import com.betterus.domain.mypage.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{

    private final MyPageRepository myPageRepository;

    @Override
    public Map<Object, Object> findMyPageDefault(Member member) {

//        myPageRepository.findMemberAndGudokByMemberId(member.getId());
//  페이징 진행중
        return null;
    }
}
