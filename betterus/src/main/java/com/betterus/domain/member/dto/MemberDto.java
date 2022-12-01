/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 10 - 22
 * 수정 일자 :
 * 기능 : Member DTO
 */

package com.betterus.domain.member.dto;

import com.betterus.domain.member.domain.Member;
import com.betterus.model.Grade;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String nickName;
    private String password;
    private String email;
    private Grade grade;
    private String user_info;

    private Long gudok_count;

    private Long gudokForCount;

    private String authorConfirmDate;

    private LocalDateTime createDate;

    public MemberDto(Member member){
        id = member.getId();
        nickName = member.getNickName();
        password = member.getPassword();
        email = member.getEmail();
        grade = member.getGrade();
        user_info = member.getUser_info();
    }

    /**
     * 회원 정보 세션 저장용
     */
    public MemberDto(Long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    /**
     * 회원 정보 마이페이지 용
     */

    public MemberDto(String nickName,Long gudok_count ,Long gudokForCount) {
        this.nickName = nickName;
        this.gudok_count = gudok_count;
        this.gudokForCount = gudokForCount;
    }

    /**
     * 관리자 페이지 회원 정보 서칭 용
     */
    public MemberDto(Long id, String nickName, Grade grade, String authorConfirmDate, LocalDateTime createDate) {
        this.id = id;
        this.nickName = nickName;
        this.grade = grade;
        this.authorConfirmDate = authorConfirmDate;
        this.createDate = createDate;
    }
}
