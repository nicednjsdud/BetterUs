/**
 * 작성자 : 정원영
 * 작성 일자 : 2022 - 11 - 04
 * 수정 일자 : 2022 - 11 - 28
 * 기능 : 마이페이지 컨트롤러
 */

package com.betterus.domain.mypage.controller;

import com.betterus.domain.article.dto.ArticleDto;
import com.betterus.domain.gudok.service.GudokService;
import com.betterus.domain.member.domain.Member;
import com.betterus.domain.member.dto.MemberDto;
import com.betterus.domain.member.repository.MemberRepository;
import com.betterus.domain.member.service.MemberService;
import com.betterus.domain.mypage.service.MyPageService;
import com.betterus.model.ArticleStatus;
import com.betterus.model.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.betterus.model.Grade.ADMIN;

@RequiredArgsConstructor
@Controller
public class MyPageController {

    private final MyPageService myPageService;

    private final MemberService memberService;

    private final GudokService gudokService;


    /**
     * 마이페이지 (유저,작가) default (list - 최신순)
     */
    @GetMapping("/myPage/default")
    public String myPageUser(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        Long sessionMemberId = (Long) member;
        if (member != null) {
            Map<Object, Object> myPage = myPageService.findMyPageDefault(sessionMemberId);
            List<ArticleDto> articleDtoList = (List<ArticleDto>) myPage.get("articleDtoList");
            if(!articleDtoList.isEmpty()) {
                ArticleDto articleDto = articleDtoList.get(0);
                if (articleDto.getStatus() == ArticleStatus.WAIT) {
                    model.addAttribute("applicationCheck", true);
                }
            }
            MemberDto memberDto = (MemberDto) myPage.get("memberDto");
            int count = (int) myPage.get("articleCount");
            model.addAttribute("articleDtoList", articleDtoList);
            model.addAttribute("memberDto", memberDto);
            model.addAttribute("articleCount", count);
            // 등급·전화 등 화면 표시에 필요한 회원 정보
            model.addAttribute("member", memberService.findMemberById(sessionMemberId));
            return "myPage/myPage(library)";
        } else {
            String msg = "회원만 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "main/main";
        }
    }

    /**
     * 마이페이지 (관리자) default (list - 최신순), 옵션 값 추가 예정
     */
    @GetMapping("/myPage/admin/memberInfo")
    public String myPageAdmin(@PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        Long sessionMemberId = (Long) member;
        Object admin = session.getAttribute("admin");
        if (sessionMemberId != null && admin != null) {
            Page<MemberDto> memberList = myPageService.findAdminPageDefault(pageable);
            int nowPage = memberList.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, memberList.getTotalPages());
            model.addAttribute("memberList", memberList);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "manager/manager(info)";
        } else {
            String msg = "관리자만 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "main/main";
        }
    }

    /**
     * 마이페이지 (관리자) 회원정보 서칭 리스트 (list - 최신순)
     */
    @GetMapping("/myPage/admin/memberInfo/search")
    public String memberListSearch(@RequestParam("searchKeyword") String keyword, @PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<MemberDto> searchMemberList = myPageService.findSearchMemberList(keyword, pageable);
        int nowPage = searchMemberList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, searchMemberList.getTotalPages());
        model.addAttribute("memberList", searchMemberList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "manager/manager(info)";
    }

    /**
     * 마이페이지 (관리자) 작가 신청 확인 글 (list - 최신순), 옵션 값 추가 예정
     */
    @GetMapping("/myPage/admin/articleConfirmCheck")
    public String myPageAdminConfirmCheck(@PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        Object admin = session.getAttribute("admin");
        if (member != null && admin != null) {
            Page<ArticleDto> articleList = myPageService.findAdminPageConfirmArticle(pageable);
            int nowPage = articleList.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, articleList.getTotalPages());
            model.addAttribute("articleList", articleList);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "manager/manager(article-check)";
        } else {
            String msg = "관리자만 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "main/main";
        }
    }

    /**
     * 마이페이지 (관리자) 회원정보 서칭 리스트 (list - 최신순)
     */
    @GetMapping("/myPage/admin/article/search")
    public String articleListSearch(HttpServletRequest request, @RequestParam("searchKeyword") String keyword, @PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        Object admin = session.getAttribute("admin");
        if (member != null && admin != null) {
            Page<ArticleDto> searchArticleList = myPageService.findSearchArticleList(keyword, pageable);
            int nowPage = searchArticleList.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, searchArticleList.getTotalPages());
            model.addAttribute("articleList", searchArticleList);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "manager/manager(article-check)";
        } else {
            String msg = "관리자만 접근이 가능합니다.";
            model.addAttribute("msg", msg);
            return "main/main";
        }
    }


    /**
     * 마이페이지 (관리자) 작가신청 회원글 1개 보기
     */
    @GetMapping("/myPage/admin/articleConfirmCheck/article/{articleId}")
    public String confirmCheckArticle(@PathVariable("articleId") Long articleId, Model model) {
        ArticleDto articleDto = myPageService.articleConfirmCheck(articleId);
        model.addAttribute("article", articleDto);
        return "manager/passOrFail";
    }

    /**
     * 마이페이지 (관리자) 작가 신청 승인
     */
    @GetMapping("/myPage/admin/pass/{memberId}")
    @Transactional
    public String authorPass(@PathVariable("memberId") Long memberId, Model model) {
        int result = myPageService.authorPass(memberId);
        String msg = "";
        if (result == 1) {
            msg = "작가 승인이 완료되었습니다.";
            model.addAttribute("msg", msg);
        }
        return "redirect:/";
    }

    /**
     * 마이페이지 (관리자) 작가 신청 불승인
     */
    @GetMapping("/myPage/admin/fail/{memberId}")
    @Transactional
    public String authorFail(@PathVariable("memberId") Long memberId, Model model) {
        int result = myPageService.authorFail(memberId);
        String msg = "";
        if (result == 1) {
            msg = "작가 승인을 취소하였습니다.";
            model.addAttribute("msg", msg);
        }
        return "redirect:/";
    }


    /**
     * 작가 상세 페이지 (패이징) 진행중
     */
    @GetMapping("/list/authorPage/{authorId}/default")
    public String authorInfo(@PathVariable("authorId") Long authorId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        Long sessionMemberId = (Long) member;
        String msg = "";
        if (sessionMemberId != null) {
            boolean check = gudokService.findGudokCheck(authorId, sessionMemberId);
            // 내페이지로 이동
            if (sessionMemberId.equals(authorId)) {
                Map<Object, Object> myPage = myPageService.findMyPageDefault(sessionMemberId);
                List<ArticleDto> articleDtoList = (List<ArticleDto>) myPage.get("articleDtoList");
                MemberDto memberDto = (MemberDto) myPage.get("memberDto");
                int count = (int) myPage.get("articleCount");
                model.addAttribute("articleDtoList", articleDtoList);
                model.addAttribute("memberDto", memberDto);
                model.addAttribute("gudokCheck", check);
                model.addAttribute("articleCount",count);
                return "myPage/myPage(library)";
            }
            // 작가 페이지로 이동
            else {
                Map<Object, Object> myPage = myPageService.findMyPageDefault(authorId);
                List<ArticleDto> articleDtoList = (List<ArticleDto>) myPage.get("articleDtoList");
                MemberDto memberDto = (MemberDto) myPage.get("memberDto");
                // 서재(MyPage)가 없어도 회원 정보는 보여 준다.
                if (memberDto == null) {
                    Member author = memberService.findMemberById(authorId);
                    if (author != null) {
                        memberDto = new MemberDto(author.getId(), author.getNickName(),
                                author.getUser_info(), author.getGudok_count(), author.getGudokForCount());
                    }
                    articleDtoList = new java.util.ArrayList<>();
                }
                model.addAttribute("articleDtoList", articleDtoList);
                model.addAttribute("memberDto", memberDto);
                model.addAttribute("gudokCheck", check);
                return "article/writerPage(article)";
            }
        } else {
            msg = "회원만 보기가 가능합니다.";
            model.addAttribute("msg", msg);
            return "main/main";
        }
    }

    /**
     * 작가 신청 하기 (마이페이지에서) — 결과는 메인으로 되돌려 한 줄로 알린다.
     */
    @PostMapping("/list/myPage/authorApplication")
    public String authorApplication(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        Object member = session.getAttribute("member");
        Long sessionMemberId = (Long) member;

        if (sessionMemberId == null) {
            redirectAttributes.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/";
        }

        Member findMember = memberService.findMemberById(sessionMemberId);
        if (findMember == null) {
            redirectAttributes.addFlashAttribute("msg", "회원 정보를 찾을 수 없습니다.");
            return "redirect:/";
        }
        // 이미 작가·관리자면 신청 대상이 아니다.
        if (findMember.getGrade() != Grade.USER) {
            redirectAttributes.addFlashAttribute("msg", "이미 작가 등급입니다.");
            return "redirect:/";
        }

        int result = myPageService.applicationInfo(sessionMemberId);
        String msg;
        if (result == 1) msg = "작가 신청이 접수되었습니다. 심사 결과를 기다려 주세요.";
        else if (result == -1) msg = "글을 3편 이상 작성한 뒤 신청할 수 있습니다.";
        else msg = "신청 처리 중 오류가 발생했습니다. 다시 시도해 주세요.";
        redirectAttributes.addFlashAttribute("msg", msg);
        return "redirect:/";
    }
}
