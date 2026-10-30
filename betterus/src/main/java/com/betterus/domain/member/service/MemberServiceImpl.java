/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 03
 * 수정 일자 : 2026 - 10 - 30
 * 기능 : 멤버 service 구현체
 */


package com.betterus.domain.member.service;

import com.betterus.domain.email.domain.Email;
import com.betterus.domain.email.repository.EmailRepository;
import com.betterus.domain.email.service.EmailService;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.dto.MemberEditForm;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.domain.mypage.domain.MyPage;
import com.betterus.domain.mypage.repository.MyPageRepository;
import com.betterus.model.Grade;
import com.betterus.model.PasswordConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final EmailRepository emailRepository;

    private final EmailService emailService;

    private final MyPageRepository myPageRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 확인 — 이메일로 회원을 찾고 비밀번호를 대조한다(BCrypt 해시/기존 평문 모두 허용)
     */
    @Override
    public Member loginConfirm(String memberEmail, String memberPassword) {
        Member findMember = memberRepository.findByEmail(memberEmail);
        if (findMember == null) return null;
        if (PasswordConfig.matches(passwordEncoder, memberPassword, findMember.getPassword())) return findMember;
        return null;
    }

    /**
     * 회원 가입 — 이메일 인증 여부·닉네임/이메일 중복을 확인하고 비밀번호를 해시로 저장한다.
     */
    @Override
    @Transactional
    public int joinMember(Member member, String authCode) {
        // 이메일 인증코드 확인 (인증 건너뛰기 설정이 켜져 있으면 통과한다)
        if (!emailService.verifyCode(member.getEmail(), authCode)) return 0;
        Email findEmail = emailRepository.findByEmail(member.getEmail());

        // 닉네임·이메일 중복 확인
        if (memberRepository.findByNickName(member.getNickName()) != null) return 0;
        if (memberRepository.findByEmail(member.getEmail()) != null) return 0;

        // 비밀번호 해시 저장
        member.changeMemberPassword(passwordEncoder.encode(member.getPassword()));
        Member joinMember = memberRepository.save(member);

        // 마이페이지(서재) 생성
        MyPage myPage = new MyPage(joinMember);
        myPageRepository.save(myPage);

        // 사용한 인증코드 무효화 (건너뛰기 상태에서는 저장된 코드가 없을 수 있다)
        if (findEmail != null) emailRepository.delete(findEmail);
        return 1;
    }

    /**
     * 닉네임 중복확인 — 사용 가능하면 1, 중복이면 0
     */
    @Override
    public int duplicateCheck(String nickName) {
        Member findMember = memberRepository.findByNickName(nickName);
        if (findMember == null) return 1;
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
     * 이메일로 찾기
     */
    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /**
     * 멤버 정보 변경 — 닉네임/이메일 중복 확인, 비밀번호는 현재 비밀번호 확인 후 해시로 저장.
     * @return 1 성공, 2 현재 비밀번호 불일치, 3 닉네임 중복, 4 이메일 중복
     */
    @Override
    @Transactional
    public int changeMemberInfo(Long memberId, MemberEditForm form) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) return 0;

        // 닉네임 변경 시 중복 확인
        String newNickName = form.getNickName();
        if (newNickName != null && !newNickName.trim().isEmpty() && !newNickName.equals(member.getNickName())) {
            Member dup = memberRepository.findByNickName(newNickName);
            if (dup != null && !dup.getId().equals(memberId)) return 3;
            member.changeMemberNickName(newNickName);
        }

        // 이메일 변경 시 중복 확인
        String newEmail = form.getEmail();
        if (newEmail != null && !newEmail.trim().isEmpty() && !newEmail.equals(member.getEmail())) {
            Member dup = memberRepository.findByEmail(newEmail);
            if (dup != null && !dup.getId().equals(memberId)) return 4;
            member.changeMemberEmail(newEmail);
        }

        // 비밀번호 변경 시 현재 비밀번호 확인 후 해시 저장
        String newPassword = form.getNewPassword();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (!PasswordConfig.matches(passwordEncoder, form.getCurrentPassword(), member.getPassword())) {
                return 2;
            }
            member.changeMemberPassword(passwordEncoder.encode(newPassword));
        }

        // 전화번호·소개글
        member.changeMemberPhone(form.getPhone());
        member.changeMemberInfo(form.getInfo());

        return 1;
    }

    /**
     * 작가 찾기
     */
    @Override
    public Page<MemberDto> findAuthorByGrade(Pageable pageable) {
        Page<Member> findMembers = memberRepository.findAuthorByGrade(Grade.AUTHOR, pageable);
        return findMembers.map(member ->
                new MemberDto(member.getId(), member.getNickName(), member.getUser_info(), member.getGudok_count(), member.getGudokForCount()));
    }

    @Override
    public Page<MemberDto> findSearchMemberList(String keyword, Pageable pageable) {
        Page<Member> findMembers = memberRepository.findAuthorByGradeAndNickNameContaining(Grade.AUTHOR, keyword, pageable);
        return findMembers.map(member ->
                new MemberDto(member.getId(), member.getNickName(), member.getUser_info(), member.getGudok_count(), member.getGudokForCount()));
    }

    /**
     * 회원 탈퇴 — 서재(MyPage)를 먼저 지우고 회원을 지운다(글·찜·구독은 회원에 딸린 cascade 로 함께 정리).
     */
    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            myPageRepository.findByMemberId(memberId).ifPresent(myPageRepository::delete);
            memberRepository.delete(member);
        }
    }

}
