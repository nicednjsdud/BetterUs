/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 :
 * 기능 : Image 테이블 엔티티
 */
package com.betterus.domain.article.domain;

import com.betterus.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "imageId")
    private Long id;

    @Column(name = "imageFileName", length = 1000)
    private String fileName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "articleId")
    private Article article;
}
