/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 :
 * 기능 : Member 테이블 엔티티
 */

package com.betterus.entity;

import com.betterus.entity.common.BaseTimeEntity;
import com.betterus.entity.common.Grade;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "memberId")
    private Long id;

    @Column(name = "nickName", length = 10)
    private String nickName;

    @Column(name = "password", length = 10)
    private String password;

    @Column(name = "email", length = 30)
    private String email;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "info", length = 1000)
    private String user_info;

    @OneToMany(mappedBy = "member")
    private List<Jjim> jjims = new ArrayList<>();
}
