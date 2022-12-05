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
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

    private String createDate;

    public MemberDto(Long id, String nickName, String password, String email, Grade grade, String user_info) {
        this.id = id;
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.grade = grade;
        this.user_info = user_info;
    }

    /**
     * 회원 정보 세션 저장용
     */
    public MemberDto(Long id, String nickName,Grade grade) {
        this.id = id;
        this.nickName = nickName;
        this.grade = grade;
    }

    /**
     * 회원 정보 마이페이지 용
     */

    public MemberDto(String nickName,Long gudok_count ,Long gudokForCount,String user_info) {
        this.nickName = nickName;
        this.gudok_count = gudok_count;
        this.gudokForCount = gudokForCount;
        this.user_info = user_info;
    }

    /**
     * 관리자 페이지 회원 정보 서칭 용
     */
    public MemberDto(Long id, String nickName, Grade grade,String email, String authorConfirmDate, String createDate) {
        this.id = id;
        this.nickName = nickName;
        this.grade = grade;
        this.email = email;
        this.authorConfirmDate = authorConfirmDate;
        this.createDate = createDate;
    }

    public MemberDto(Long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    /**
     * 작가 둘러보기 페이지 용
     */
    public MemberDto(Long id, String nickName, String user_info, Long gudok_count, Long gudokForCount) {
        this.id = id;
        this.nickName = nickName;
        this.user_info = user_info;
        this.gudok_count = gudok_count;
        this.gudokForCount = gudokForCount;
    }
}
