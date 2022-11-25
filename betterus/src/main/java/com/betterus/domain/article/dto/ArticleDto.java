/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 06
 * 수정 일자 : 2022 - 11 - 23
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

    private Long reviewCount;

    private Long jjimCount;

    /**
     * 리스트 페이징 용
     */
    public ArticleDto(String title, String subTitle, String contents, ArticleStatus status, Long reviewCount, Long jjimCount) {
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.status = status;
        this.reviewCount = reviewCount;
        this.jjimCount = jjimCount;
    }
}
