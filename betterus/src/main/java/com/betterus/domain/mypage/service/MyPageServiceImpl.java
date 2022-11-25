package com.betterus.domain.mypage.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.mypage.domain.MyPage;
import com.betterus.domain.mypage.repository.MyPageRepository;
import com.betterus.model.ArticleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final MyPageRepository myPageRepository;

    @Override
    public Map<Object, Object> findMyPageDefault(Member member) {

//        myPageRepository.findMemberAndGudokByMemberId(member.getId());
//  페이징 진행중
        return null;
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
                ArticleDto articleDto =
                        new ArticleDto(article.getTitle(), article.getSubTitle(), article.getContents(), article.getStatus(), article.getReviewCount(), article.getJjimCount());
                articleDtoList.add(articleDto);
            }
            /** 회원정보 가져오기 */
            Member member = myPage.getMember();
            MemberDto memberDto = new MemberDto(member.getNickName(), member.getGudok_count(), member.getGudokForCount());

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
            /** 출간된 article이 3개 이상일 때*/
            if (myPage.getArticleList().size() >= 3) {
                /** 게시글 들만 승인 대기중으로 변경*/
                List<Article> articleList = myPage.getArticleList();
                for (Article article : articleList) {
                    article.changeArticleStatus(ArticleStatus.WAIT);
                }
                return 1;
            } else {
                /** 3개미만의 글은 신청 x*/
                return -1;
            }
        }
        else{
            return 0;
        }
    }
}
