package com.betterus.domain.gudok.service;

import com.betterus.domain.gudok.domain.Gudok;
import com.betterus.domain.gudok.repository.GudokRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GudokServiceImpl implements GudokService {

    private final GudokRepository gudokRepository;

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public int addGudok(Member member, Long authorId) {
        Gudok gudok = gudokRepository.findGudokTure(authorId, member.getId());
        if (gudok == null) {
            Optional<Member> findMember = memberRepository.findById(member.getId());
            Optional<Member> findAuthor = memberRepository.findById(authorId);
            if (findMember.isPresent() && findAuthor.isPresent()) {
                Member trueMember = findMember.get();
                Member trueAuthor = findAuthor.get();
                gudokRepository.save(new Gudok(authorId, trueMember));
                trueAuthor.changeGudokCount(trueAuthor.getGudok_count(),"구독추가");
                return 1;
            } else {
                // 아이디 찾기 오류
                return 0;
            }
        } else {
            // 중복된 추가가 있음
            return 2;
        }
    }

    @Override
    public int deleteGudok(Member member, Long authorId) {
        Gudok gudok = gudokRepository.findGudokTure(authorId, member.getId());
        if (gudok != null) {
            gudokRepository.delete(gudok);
            Gudok gudokCheck = gudokRepository.findGudokTure(authorId, member.getId());
            Optional<Member> findAuthor = memberRepository.findById(authorId);
            if(gudokCheck == null && findAuthor.isPresent()){
                Member trueAuthor = findAuthor.get();
                trueAuthor.changeGudokCount(trueAuthor.getGudok_count(),"구독삭제");
                return 1;
            }
            else{
                // 삭제 되지 않음
                return 2;
            }
        } else {
            // 중복 삭제 불가
            return 0;
        }
    }
}
