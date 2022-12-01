/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 06
 * 수정 일자 : 2022 - 11 - 25
 * 기능 : Article Controller
 */

package com.betterus.domain.article.controller;

import com.betterus.domain.article.domain.Article;
import com.betterus.domain.article.domain.Image;
import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.article.dto.ArticleForm;
import com.betterus.domain.article.service.ArticleService;
import com.betterus.domain.article.service.ImageService;
import com.betterus.domain.gudok.service.GudokService;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.service.MemberService;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final ImageService imageService;


    /**
     * 글 목록 리스트 (패이징) 진행중
     */
    @RequestMapping("/list/article")
    public String articleListBasic(@PageableDefault(size = 10) Pageable pageable, Model model, HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            Page<ArticleDto> articleList = articleService.findArticleList(pageable);
            int nowPage = articleList.getPageable().getPageNumber();
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, articleList.getTotalPages());
            model.addAttribute("list", articleList);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "화면 구현 안됨";
        } else {
            msg = "회원만 리스트 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "direct:/";
        }
    }

    /**
     * 서치 목록 리스트 (패이징) 진행중
     */
    @GetMapping("/list/search")
    public String articleListSearch(@RequestParam("searchKeyword") String keyword, @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<ArticleDto> searchArticleList = articleService.findSearchArticleList(keyword, pageable);
        int nowPage = searchArticleList.getPageable().getPageNumber();
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, searchArticleList.getTotalPages());
        model.addAttribute("list", searchArticleList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "article/articles";
    }


    /**
     * 글 상세보기
     */
    @GetMapping("/list/article/{articleId}")
    public String articleInfo(@PathVariable("articleId") Long articleId, Model model, HttpServletRequest request) {
        String msg = "";
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            Article findArticle = articleService.findArticle(articleId);
            model.addAttribute("article", findArticle);
        } else {
            msg = "회원만 보기가 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
        return "article/a_article";
    }


    /**
     * 글 쓰기 폼 (이미지 업로드 기능 추가예정)
     */
    @GetMapping("/write")
    public String writeArticleForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        if (member != null) {
            model.addAttribute("articleForm", new ArticleForm());
            return "writing/writing";
        } else {
            String msg = "회원만 글쓰기가 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }

    }

    /**
     * 글 쓰기 (이미지 업로드 기능 추가예정)
     */
    @PostMapping("/write")
    @ResponseStatus(HttpStatus.CREATED)
    public String writeArticle(ArticleForm articleForm, Model model, HttpServletRequest request) throws Exception {
        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            int result = articleService.saveArticle(articleForm, member,articleForm.getFiles());
            if (result == 1) {
                msg = "글쓰기가 완료되었습니다.";
                model.addAttribute("msg", msg);
                return "myPage/myPage(info)";
            } else {
                msg = "오류가 발생했습니다. 다시 시도해주세요.";
                model.addAttribute("msg", msg);
                return "writing/writing";
            }
        } else {
            msg = "회원만 글쓰기가 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }

    /**
     * 글 수정 폼 (이미지 업로드 기능 추가예정)
     */
    @GetMapping("myPage/{articleId}/edit")
    public String updateArticleForm(@PathVariable("articleId") Long articleId, Model model) {
        Article article = articleService.findArticle(articleId);

        ArticleForm articleForm = new ArticleForm(article.getTitle(), article.getSubTitle(), article.getContents(),null);
        model.addAttribute("articleForm", articleForm);
        return "writing/writing(correction)";
    }

    /**
     * 글 수정 (이미지 업로드 기능 추가예정)
     */
    @PostMapping("myPage/{articleId}/edit")
    public String updateArticle(@PathVariable("articleId") Long articleId, @ModelAttribute("form") ArticleForm articleForm,
                                HttpServletRequest request, Model model) throws Exception {
        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        // DB에 저장되어있는 파일 불러오기
        List<Image> dbImgList = imageService.findAllByArticle(articleId);
        // 전달되어온 파일들
        List<MultipartFile> multipartFileList = articleForm.getFiles();
        // 새롭게 전달되어온 파일들의 목록을 저장할 List 선언
        List<MultipartFile> addFileList = new ArrayList<>();
        if (member != null) {
            if(CollectionUtils.isEmpty(multipartFileList)){
                // 전달되어온 파일이 없으면
                for (Image dbImage : dbImgList) {
                    imageService.deleteImage(dbImage.getId());
                }
            }
            else{
                // 파일이 하나 이상 존재하면
                List<String> dbOriginNameList = new ArrayList<>();

                // DB의 파일 원본명 추출
                for (Image dbImage : dbImgList) {
                    // file id로 DB에 저장된 파일 정보 얻어오기
                    Image findImage = imageService.findByImageId(dbImage.getId());
                    // DB의 파일 원본명 얻어오기
                    String dbOrigFileName = findImage.getOrigFileName();
                    // 서버에 저장된 파일들 중 전달되어온 파일이 존재하지 않는다면
                    if(!multipartFileList.contains(dbOrigFileName))
                        // 파일 삭제
                            imageService.deleteImage(dbImage.getId());
                    // 그것도 아니라면
                    else dbOriginNameList.add(dbOrigFileName);  // DB에 저장할 파일 목록에 추가
                }
                for (MultipartFile multipartFile : multipartFileList) {
                    // 전달되어온 파일 하나씩 검사
                    // 파일의 원본명 얻기
                    String multipartFileOrigName = multipartFile.getOriginalFilename();
                    if(!dbOriginNameList.contains(multipartFileOrigName)){
                        // DB에 없는 파일이라면
                        addFileList.add(multipartFile); // DB에 저장할 파일 목록에 추가
                    }
                }
            }
            int result = articleService.updateArticle(articleId, articleForm,addFileList);
            if (result == 1) {
                msg = "글 수정이 완료되었습니다.";
                model.addAttribute("msg", msg);
                return "myPage/myPage(info)";
            } else {
                msg = "오류가 발생했습니다. 다시 시도해주세요.";
                model.addAttribute("msg", msg);
                // 확인후 수정
                return "writing/writing(correction)";
            }
        } else {
            msg = "회원만 수정이 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }

    /**
     * 글 삭제 (이미지 업로드 기능 추가예정)
     */
    @PostMapping("myPage/{articleId}/delete")
    public String updateArticle(@PathVariable("articleId") Long articleId,
                                HttpServletRequest request, Model model) {
        String msg = "";
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            int result = articleService.deleteArticle(articleId);
            if (result == 1) {
                msg = "글 삭제가 완료되었습니다.";
            } else {
                msg = "오류가 발생했습니다. 다시 시도해주세요.";
            }
            model.addAttribute("msg", msg);
            return "myPage/myPage(info)";
        } else {
            msg = "회원만 삭제가 가능합니다.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        }
    }
}
