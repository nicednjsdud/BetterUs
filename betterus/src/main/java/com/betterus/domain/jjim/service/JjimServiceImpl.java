package com.betterus.domain.jjim.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.gudok.domain.Gudok;
import com.betterus.domain.jjim.domain.Jjim;
import com.betterus.domain.jjim.repository.JjimRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class JjimServiceImpl implements JjimService {

    private final JjimRepository jjimRepository;

    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public int addJjim(Long memberId, Long articleId) {
        Jjim jjim = jjimRepository.findByArticleIdAndMemberId(articleId, memberId);
        if (jjim == null) {
            Article findArticle = articleRepository.findArticleById(articleId);
            Optional<Member> member = memberRepository.findById(memberId);
            if(member.isPresent()){
                Member findMember = member.get();
                jjim = new Jjim(findMember, findArticle);
                findArticle.changeJjimCount("찜추가");
                jjimRepository.save(jjim);
            }
            return 1;
        } else {
            // 중복된 추가가 있음
            return 2;
        }
    }

    @Override
    @Transactional
    public int deleteJjim(Long memberId, Long articleId) {
        Jjim jjim = jjimRepository.findByArticleIdAndMemberId(articleId, memberId);
        jjimRepository.deleteById(jjim.getId());
        Article findArticle = jjim.getArticle();
        findArticle.changeJjimCount("찜삭제");
        return 1;
    }

    @Override
    public Boolean findJjimTure(Long articleId, Long memberId) {
        Jjim findJjim = jjimRepository.findByArticleIdAndMemberId(articleId, memberId);
        if(findJjim == null) return false;
        else return true;
    }
}
