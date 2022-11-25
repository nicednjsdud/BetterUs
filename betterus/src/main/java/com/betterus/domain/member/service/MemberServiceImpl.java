/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 :
 * 기능 : 멤버 service 구현체
 */


package com.betterus.domain.member.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.email.domain.Email;
import com.betterus.domain.email.repository.EmailRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.dto.MemberEditForm;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.domain.mypage.domain.MyPage;
import com.betterus.domain.mypage.repository.MyPageRepository;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final EmailRepository emailRepository;

    private final MyPageRepository myPageRepository;

    private final EntityManager em;

    /**
     * 로그인 확인
     */
    @Override
    public Member loginConfirm(String memberEmail, String memberPassword) {
        Member findMember = memberRepository.findByEmailAndPassword(memberEmail, memberPassword);
        if (findMember != null) return findMember;
        else return null;
    }

    /**
     * 회원 가입
     */
    @Override
    @Transactional
    public int joinMember(Member member, String authCode) {
        Member duplicateEmail = memberRepository.findByEmail(member.getEmail());
        Email findEmail = emailRepository.findByEmail(member.getEmail());

        if (duplicateEmail == null && findEmail.getAuthCode().equals(authCode)) {
            Member joinMember = memberRepository.save(member);
            // 마이페이지 생성
            MyPage myPage = new MyPage(member);
            myPageRepository.save(myPage);
            return 1;
        } else return 0;
    }

    /**
     * 닉네임 중복확인
     */
    @Override
    public int duplicateCheck(String nickName) {
        Member findMember = memberRepository.findByNickName(nickName);
        if (findMember != null) return 1;
        else return 0;
    }

    /**
     * 멤버 아이디로 찾기
     */
    @Override
    public Member findMemberById(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        return findMember.orElse(null);
    }

    /**
     * 멤버 정보 변경
     */

    // 변경감지안됨 확인
    @Override
    @Transactional
    public int changeMemberInfo(Long memberId, MemberEditForm form) {
        Member member = em.find(Member.class, memberId);
        String action = form.getAction();
        if (Objects.equals(action, "닉네임변경")) {
            member.changeMemberNickName(form.getNickName());
            return 1;
        } else if (Objects.equals(action, "비밀번호변경")) {
            String findPrePassword = member.getPassword();
            if (findPrePassword.equals(form.getPrePassword())) {
                member.changeMemberPassword(form.getAfterPassword());
                return 1;
            } else {
                // 기존 비밀번호와 현재 적은 비밀번호가 다름
                return 2;
            }
        } else if (Objects.equals(action, "회원소개변경")) {
            member.changeMemberInfo(form.getInfo());
            return 1;
        }

        return 0;
    }

    /**
     * 작가 찾기
     */
    @Override
    public Page<MemberDto> findAuthorByGrade(Pageable pageable) {
        Page<MemberDto> findMembers = memberRepository.findAuthorByGrade(Grade.AUTHOR, pageable);
//        Page<ArticleDto> articleDtos = findArticles.map(article ->
//                new ArticleDto(article.getId(),article.getTitle(),article.getSubTitle(),article.getSubTitle(),article.getStatus()));
        return findMembers;
    }

}
