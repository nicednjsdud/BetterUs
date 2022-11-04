/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 :
 * 기능 : Member 테이블 엔티티
 */

package com.betterus.domain.member.domain;

import com.betterus.domain.jjim.domain.Jjim;
import com.betterus.model.BaseTimeEntity;
import com.betterus.model.Grade;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    @NotNull
    private String nickName;

    @Column(name = "password", length = 10)
    @NotNull
    private String password;

    @Column(name = "email", length = 30, unique = true)
    @NotNull
    private String email;

    @Column(name = "phone",length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "info", length = 1000)
    private String user_info;

    @OneToMany(mappedBy = "member")
    private List<Jjim> jjims = new ArrayList<>();


    public Member(String nickName, String password, String email,Grade grade) {
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.grade = grade;
    }
}
