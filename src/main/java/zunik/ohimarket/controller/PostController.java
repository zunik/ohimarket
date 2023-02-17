package zunik.ohimarket.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zunik.ohimarket.constant.SessionConst;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.dto.PostCreateDto;
import zunik.ohimarket.service.MemberService;
import zunik.ohimarket.service.PostCategoryService;
import zunik.ohimarket.service.PostLikeService;
import zunik.ohimarket.service.PostService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {
    private final PostCategoryService postCategoryService;
    private final PostService postService;
    private final MemberService memberService;
    private final PostLikeService postLikeService;

    @GetMapping("/add")
    public String addForm(Model model) {
        List<PostCategory> postCategories = postCategoryService.findAll();

        model.addAttribute("form", new PostCreateDto());
        model.addAttribute("postCategories", postCategories);

        return "post/addForm";
    }

    @PostMapping("/add")
    public String addPost(@Validated @ModelAttribute("form") PostCreateDto form,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "post/addForm";
        }

        Post post = new Post();
        post.setPostCategoryName(form.getPostCategoryName());
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setMemberId(loginMember.getId());
        postService.save(post);

        return "redirect:/";
    }

    @GetMapping("/{postId}")
    public String postDetail(
            @PathVariable long postId,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
    ) {
        Post post = postService.findById(postId).orElseThrow();
        PostCategory postCategory = postCategoryService.findById(post.getPostCategoryName()).get();
        Member member = memberService.findById(post.getMemberId()).get();

        boolean isLike = postLikeService.PostLikeCheck(
                post.getId(), loginMember.getId()
        );

        visit(post.getId(), request, response);

        model.addAttribute("isLike", isLike);
        model.addAttribute("postCategory", postCategory);
        model.addAttribute("post", post);
        model.addAttribute("member", member);
        return "post/detailView";
    }

    @PostMapping("/delete")
    public String deletePost(
            @RequestParam(value = "postId") Long postId,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
            ) {
        postService.delete(postId, loginMember.getId());
        return "redirect:/";
    }

    /**
     * 조회수 처리
     * 조회수 중복 처리 방지
     */
    private void visit(Long id, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String cookieName = "postView";
        String postIdCookieValue = "[" + id + "]";
        Cookie postViewsCookie = null;

        for (Cookie cookie: cookies) {
            if (cookie.getName().equals(cookieName)) {
                postViewsCookie = cookie;
            }
        }

        if (postViewsCookie != null && postViewsCookie.getValue().contains(postIdCookieValue)) {
            return;
        }

        // DB 조회수 올리기
        postService.increaseViews(id);

        if (postViewsCookie == null) {
            postViewsCookie = new Cookie(cookieName, postIdCookieValue);
        } else {
            postViewsCookie.setValue(postViewsCookie.getValue() + postIdCookieValue);
        }

        postViewsCookie.setPath("/");
        postViewsCookie.setMaxAge(3600);
        response.addCookie(postViewsCookie);
    }
}
