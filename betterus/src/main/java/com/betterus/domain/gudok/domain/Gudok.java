/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 :
 * 기능 : Gudok 테이블 엔티티
 */
package com.betterus.domain.gudok.domain;

import com.betterus.domain.member.domain.Member;
import com.betterus.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class Gudok extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "gudokId")
    private Long id;

    @Column(name = "authorId")
    private Long authorId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    /**
     * 구독 추가 폼
     */
    public Gudok(Long authorId, Member member) {
        this.authorId = authorId;
        this.member = member;
    }
}
