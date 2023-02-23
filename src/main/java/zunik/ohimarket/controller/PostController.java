package zunik.ohimarket.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zunik.ohimarket.constant.SessionConst;
import zunik.ohimarket.controller.dto.PostUpdateDto;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.controller.dto.PostCreateDto;
import zunik.ohimarket.exception.AccessDeniedException;
import zunik.ohimarket.service.dto.PostDetailResponseDto;
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

        postService.save(form, loginMember.getId());

        return "redirect:/";
    }

    @GetMapping("/{postToken}")
    public String postDetail(
            @PathVariable String postToken,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
    ) {
        PostDetailResponseDto responseDto = postService.getDetail(postToken);
        Long postId = responseDto.getPost().getId();

        visit(postId, request, response);

        boolean isLike = postLikeService.PostLikeCheck(
                postId, loginMember.getId()
        );

        model.addAttribute("isLike", isLike);
        model.addAttribute("model", responseDto);
        return "post/detailView";
    }

    @GetMapping("/{postToken}/edit")
    public String editForm(
            @PathVariable String postToken,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model
    ) {
        Post post = postService.findByToken(postToken);
        List<PostCategory> postCategories = postCategoryService.findAll();

        if (post.getMemberId() != loginMember.getId()) {
            // 해당 게시글에 수정권한이 없을경우
            throw new AccessDeniedException();
        }

        PostUpdateDto form = new PostUpdateDto();

        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        form.setPostCategoryName(post.getPostCategoryName());

        model.addAttribute("form", form);
        model.addAttribute("post", post);
        model.addAttribute("postCategories", postCategories);

        return "post/editForm";
    }

    @PostMapping("/{postToken}/edit")
    public String editPost(
            @PathVariable String postToken,
            @Validated @ModelAttribute("form") PostUpdateDto form,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "post/editForm";
        }

        postService.update(
                postToken, loginMember.getId(),
                form
        );

        redirectAttributes.addAttribute("postToken", postToken);
        return "redirect:/post/{postToken}";
    }

    @PostMapping("/delete")
    public String deletePost(
            @RequestParam(value = "postToken") String postToken,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
    ) {
        postService.delete(postToken, loginMember.getId());
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
