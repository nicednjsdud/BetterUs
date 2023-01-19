/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 15
 * 수정 일자 : 2022 - 11 - 28
 * 기능 : 마이페이지 로직 처리
 */

package com.betterus.domain.mypage.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.domain.Image;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.article.repository.ImageRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.domain.mypage.domain.MyPage;
import com.betterus.domain.mypage.repository.MyPageRepository;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final MyPageRepository myPageRepository;

    private final MemberRepository memberRepository;

    private final ArticleRepository articleRepository;

    private final ImageRepository imageRepository;

    @Override
    public Map<Object, Object> findMyPageDefault(Long id) {
        Optional<MyPage> findMyPage = myPageRepository.findByMemberId(id);
        List<Article> findArticle = articleRepository.findByMemberId(Sort.by(Sort.Direction.DESC, "createDate"), id);
        int articleCount = findArticle.size();
        Map<Object, Object> map = new HashMap<>();
        if (findMyPage.isPresent()) {
            MyPage myPage = findMyPage.get();
            /** 모든 아티클 가져오기 */
            List<ArticleDto> articleDtoList = new ArrayList<>();
            for (Article article : findArticle) {
                Image image = imageRepository.findByArticle(article);
                String path = null;
                if (image != null) path = image.getFullPath();
                ArticleDto articleDto =
                        new ArticleDto(article.getId(), article.getTitle(), article.getSubTitle(), article.getContents(),
                                article.getStatus(), article.getReviewCount(), article.getJjimCount(), path);
                articleDtoList.add(articleDto);
            }
            /** 회원정보 가져오기 */
            Member member = myPage.getMember();
            MemberDto memberDto = new MemberDto(member.getId(), member.getNickName(), member.getGudok_count(),
                    member.getGudokForCount(), member.getUser_info());

            map.put("articleDtoList", articleDtoList);
            map.put("memberDto", memberDto);
            map.put("articleCount", articleCount);
        }
        return map;

    }

    @Override
    public Map<Object, Object> findAuthorById(Long authorId) {
        Optional<MyPage> findAuthorPage = myPageRepository.findByMemberId(authorId);
        Map<Object, Object> map = new HashMap<>();
        if (findAuthorPage.isPresent()) {
            MyPage myPage = findAuthorPage.get();
            List<Article> articleList = myPage.getArticleList();

            /** 모든 아티클 가져오기 */
            List<ArticleDto> articleDtoList = new ArrayList<>();
            for (Article article : articleList) {
                Image image = imageRepository.findByArticle(article);
                String path = null;
                if (image != null) path = image.getFullPath().substring(25);
                ArticleDto articleDto =
                        new ArticleDto(article.getId(), article.getTitle(), article.getSubTitle(), article.getContents(), article.getStatus(), article.getReviewCount(), article.getJjimCount(), path);
                articleDtoList.add(articleDto);
            }
            /** 회원정보 가져오기 */
            Member member = myPage.getMember();
            MemberDto memberDto = new MemberDto(member.getId(), member.getNickName(), member.getGudok_count(), member.getGudokForCount(), member.getUser_info());

            map.put("articleDtoList", articleDtoList);
            map.put("memberDto", memberDto);
        }
        return map;
    }

    @Override
    @Transactional
    public int applicationInfo(Long sessionMemberId) {
        Optional<MyPage> findMyPage = myPageRepository.findByMemberId(sessionMemberId);
        if (findMyPage.isPresent()) {
            MyPage myPage = findMyPage.get();

            /** 출간된 article이 3개 이상일 때 */
            if (myPage.getArticleList().size() >= 3 && myPage.getMember().getGrade() == Grade.USER) {
                /** 게시글 들만 승인 대기중으로 변경 */
                List<Article> articleList = myPage.getArticleList();
                String msg = "작가신청";
                myPage.getMember().changeConfirmDate();
                for (Article article : articleList) {
                    article.changeArticleStatus(msg);
                }
                return 1;
            } else return -1; /** 3개미만의 글은 신청 x */
        } else return 0;
    }

    /**
     * 옵션 적용 안한 기본 멤버 리스트
     */
    @Override
    public Page<MemberDto> findAdminPageDefault(Pageable pageable) {

        Page<MemberDto> findArticles = memberRepository.findAll(pageable).map(member ->
                new MemberDto(member.getId(), member.getNickName(), member.getGrade(), member.getEmail(), member.getAuthorConfirmDate(), member.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        return findArticles;
    }

    /**
     * 멤버 서칭 리스트
     */
    @Override
    public Page<MemberDto> findSearchMemberList(String keyword, Pageable pageable) {
        Page<Member> findMembers = memberRepository.findSearchListByNickNameContaining(keyword, pageable);
        Page<MemberDto> MemberDtos = findMembers.map(member ->
                new MemberDto(member.getId(), member.getNickName(), member.getGrade(), member.getEmail(), member.getAuthorConfirmDate(), member.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd"))));
        return MemberDtos;
    }

    /**
     * 옵션 적용 안한 기본 article confirm 리스트
     */
    @Override
    public Page<ArticleDto> findAdminPageConfirmArticle(Pageable pageable) {
        Page<Article> articleList = articleRepository.findConfirmArticleByStatus(ArticleStatus.WAIT, pageable);
        Page<ArticleDto> articleDtos = articleList.map(article ->
                new ArticleDto(article.getId(), article.getTitle(), article.getStatus(), article.getMember().getNickName(), article.getMember().getEmail(), article.getMember().getAuthorConfirmDate()));
        return articleDtos;
    }


    /**
     * 관리자 페이지 article 하나 보이기 용
     */
    @Override
    public ArticleDto articleConfirmCheck(Long articleId) {
        Article article = articleRepository.findArticleById(articleId);
        String createDate = article.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ArticleDto articleDto = new ArticleDto(article.getId(), article.getMember().getId(), article.getTitle(), article.getSubTitle(), article.getContents(), article.getMember().getNickName(), createDate, article.getMember().getUser_info());
        return articleDto;
    }

    /**
     * 관리자 페이지 회원 작가 신청 승인
     */
    @Override
    @Transactional
    public int authorPass(Long memberId) {
        Optional<MyPage> findMyPage = myPageRepository.findByMemberId(memberId);
        if (findMyPage.isPresent()) {
            MyPage userPage = findMyPage.get();
            List<Article> articleList = userPage.getArticleList();
            for (Article article : articleList) {
                article.changeArticleStatus("작가승인");
            }
            userPage.getMember().changeGrade(Grade.AUTHOR);
        }
        return 1;
    }

    /**
     * 관리자 페이지 회원 작가 신청 불승인
     */
    @Override
    @Transactional
    public int authorFail(Long memberId) {
        Optional<MyPage> findMyPage = myPageRepository.findByMemberId(memberId);
        if (findMyPage.isPresent()) {
            MyPage userPage = findMyPage.get();
            List<Article> articleList = userPage.getArticleList();
            for (Article article : articleList) {
                article.changeArticleStatus("작가불승인");
            }
        }
        return 1;
    }

    @Override
    public Page<ArticleDto> findSearchArticleList(String keyword, Pageable pageable) {
        Page<Article> findArticles = articleRepository.findSearchListByTitleContaining(keyword, pageable);
        Page<ArticleDto> articleDtos = findArticles.map(article ->
                new ArticleDto(article.getId(), article.getTitle(), article.getStatus(), article.getMember().getNickName(), article.getMember().getEmail(), article.getMember().getAuthorConfirmDate()));
        return articleDtos;
    }
}
