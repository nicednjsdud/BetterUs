package com.betterus.domain.article.service;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.dto.ArticleForm;
import com.betterus.domain.article.repository.ArticleRepository;
import com.betterus.domain.member.domain.Member;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;

    @Override
    public int saveArticle(ArticleForm articleForm, Member member) {
        Article savedArticle = null;
        if(member.getGrade().equals(Grade.USER)) {
            Article article = new Article(articleForm.getTitle(), articleForm.getSubTitle(), articleForm.getContents(), ArticleStatus.WAIT, member);
            savedArticle = articleRepository.save(article);
        }
        else if(member.getGrade().equals(Grade.AUTHOR)){
            Article article = new Article(articleForm.getTitle(), articleForm.getSubTitle(), articleForm.getContents(), ArticleStatus.APPROVAL, member);
            articleRepository.save(article);
            savedArticle = articleRepository.save(article);
        }
        if(savedArticle != null){
            return 1;
        }
        else{
            return 0;
        }
    }
}
