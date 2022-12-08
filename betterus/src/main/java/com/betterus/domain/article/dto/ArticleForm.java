/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 06
 * 수정 일자 : 2022 - 11 - 08
 * 기능 : Article form
 */

package com.betterus.domain.article.dto;

import com.betterus.domain.article.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleForm {

    private Long id;
    private String title;
    private String subTitle;
    private String contents;
    private List<MultipartFile> files;
}
