/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 06
 * 수정 일자 : 2022 - 12 - 02
 * 기능 : Article dto 화면 뿌리기용
 */

package com.betterus.domain.article.dto;

import com.betterus.domain.article.domain.Image;
import com.betterus.domain.articleseries.domain.ArticleSeries;
import com.betterus.domain.member.domain.Member;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticleDto {

    private Long id;

    private Long memberId;
    private String title;
    private String subTitle;
    private String contents;
    private ArticleStatus status;

    private String email;

    private Long reviewCount;

    private Long jjimCount;

    private String nickName;

    private Grade grade;

    private String authorConfirmDate;

    private String createDate;

    private String user_info;

    private String imageFullPath;

    private List<MultipartFile> multipartFiles;


    /**
     * 리스트 페이징 용
     */
    @QueryProjection
    public ArticleDto(Long id, String title, String subTitle, String contents, ArticleStatus status, Long reviewCount, Long jjimCount, String imageFullPath) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.status = status;
        this.reviewCount = reviewCount;
        this.jjimCount = jjimCount;
        this.imageFullPath = imageFullPath;
    }
    /**
     * 메인 페이지 용
     */
    @QueryProjection
    public ArticleDto(Long id, String title, String subTitle, String contents, ArticleStatus status, Long reviewCount, Long jjimCount, String imageFullPath,String nickName) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.status = status;
        this.reviewCount = reviewCount;
        this.jjimCount = jjimCount;
        this.imageFullPath = imageFullPath;
        this.nickName = nickName;
    }

    /**
     * 관리자 페이지 작가 신청 확인 article
     */

    public ArticleDto(Long id, String title, ArticleStatus status, String nickName, String email, String authorConfirmDate) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.nickName = nickName;
        this.email = email;
        this.authorConfirmDate = authorConfirmDate;
    }

    /**
     * 관리자 페이지 article 한개 보이기 용
     */

    public ArticleDto(Long id, Long memberId, String title, String subTitle, String contents, String nickName, String createDate, String user_info) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.nickName = nickName;
        this.createDate = createDate;
        this.user_info = user_info;
    }

    /**
     * article 상세 보기 (1개) 폼
     */
    public ArticleDto(Long id, Long memberId, String title, String subTitle, String contents, Long reviewCount, Long jjimCount, String nickName, String userInfo, String createDate, String fullPath) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.reviewCount = reviewCount;
        this.jjimCount = jjimCount;
        this.nickName = nickName;
        this.createDate = createDate;
        this.memberId = memberId;
        this.user_info = userInfo;
        this.imageFullPath = fullPath;
    }

    /**
     * article 수정 폼용
     */
    public ArticleDto(Long id, String title, String subTitle, String contents, List<MultipartFile> multipartFiles) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.contents = contents;
        this.multipartFiles = multipartFiles;
    }
}
