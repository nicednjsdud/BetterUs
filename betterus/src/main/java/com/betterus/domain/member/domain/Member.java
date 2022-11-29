/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 19
 * 수정 일자 : 2022 - 11 - 18
 * 기능 : Member 테이블 엔티티
 */

package com.betterus.domain.member.domain;


import com.betterus.domain.gudok.domain.Gudok;
import com.betterus.domain.jjim.domain.Jjim;
import com.betterus.model.ArticleStatus;
import com.betterus.model.BaseTimeEntity;
import com.betterus.model.Grade;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Column(name = "email", length = 30)
    @NotNull
    private String email;

    @Column(name = "phone",length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "info", length = 1000)
    private String user_info;

    @Column(name = "gudokCount")
    private Long gudok_count;

    @Column(name = "gudokForCount")
    private Long gudokForCount;

    @Column(name = "authorConfirmDate")
    private String authorConfirmDate;


    @OneToMany(mappedBy = "member")
    private List<Jjim> jjims = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Gudok> gudoks = new ArrayList<>();


    /**
     *  구독 count 와 관심작가 count 초기 0 설정
     */
    @PrePersist
    public void prePersist(){
        if(this.gudok_count == null && this.gudokForCount == null){
            this.gudok_count = 0L;
            this.gudokForCount = 0L;
        }
    }


    public Member(String nickName, String password, String email,Grade grade) {
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.grade = grade;
    }

    /**
     * 닉네임 변경
     */
    public void changeMemberNickName(String nickName){
        this.nickName = nickName;
    }

    /**
     * 회원 소개변경
     */
    public void changeMemberInfo(String user_info){
        this.user_info = user_info;
    }

    /**
     * 비밀번호 변경 (임시 비밀번호 포함)
     */
    public void changeMemberPassword(String password){
        this.password = password;
    }

    /**
     * 구독 카운트 변경
     */
    public void changeGudokCount(Long gudok_count,String msg){
        if(msg == "구독추가") this.gudok_count += gudok_count;
        else if(msg == "구독삭제"){
            if(this.gudok_count !=0L)this.gudok_count -= gudok_count;
            else this.gudok_count = 0L;
        }
    }

    /**
     * 관심작가 카운트 변경
     */
    public void changeGudokForCount(Long gudokForCount,String msg){
        if(msg == "구독추가") this.gudokForCount += gudokForCount;
        else if(msg == "구독삭제"){
            if(this.gudokForCount !=0L)this.gudokForCount -= gudokForCount;
            else this.gudokForCount = 0L;
        }
    }

    /**
     * 등급 변경 (작가, 일반회원, 관리자)
     */
    public void changeGrade(Grade grade){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());
        this.authorConfirmDate = strToday;
        this.grade = grade;
    }
}
