package com.betterus.domain.jjim.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.gudok.domain.Gudok;
import com.betterus.domain.jjim.domain.Jjim;
import com.betterus.domain.jjim.repository.JjimRepository;
import com.betterus.domain.member.domain.Member;
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

    @Override
    @Transactional
    public int addJjim(Member member, Long articleId) {
        Jjim jjim = jjimRepository.findByArticleIdAndMemberId(articleId, member.getId());
        if (jjim == null) {
            Article findArticle = articleRepository.findArticleById(articleId);
            jjim = new Jjim(member, findArticle);
            findArticle.changeJjimCount("찜추가");
            jjimRepository.save(jjim);

            return 1;
        } else {
            // 중복된 추가가 있음
            return 2;
        }
    }

    @Override
    @Transactional
    public int deleteJjim(Member member, Long articleId) {
        Jjim jjim = jjimRepository.findByArticleIdAndMemberId(articleId, member.getId());
        jjimRepository.deleteById(jjim.getId());
        return 1;
    }
}
