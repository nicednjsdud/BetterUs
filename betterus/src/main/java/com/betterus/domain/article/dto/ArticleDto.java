/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 06
 * 수정 일자 :
 * 기능 : Article dto 화면 뿌리기용
 */

package com.betterus.domain.article.dto;

import com.betterus.domain.articleseries.domain.ArticleSeries;
import com.betterus.domain.member.domain.Member;
import com.betterus.model.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ArticleDto {

    private Long id;
    private String title;
    private String subTitle;
    private String contents;
    private ArticleStatus status;
}
