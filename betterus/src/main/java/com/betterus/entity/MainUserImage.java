/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 :
 * 기능 : MainUserImage 테이블 엔티티
 */

package com.betterus.entity;

import com.betterus.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "MainImage")
public class MainUserImage extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "mainImageId")
    private Long id;

    @Column(name = "fileName", length = 1000)
    private String fileName;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
