package zunik.ohimarket.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import zunik.ohimarket.constant.SessionConst;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.dto.PostCreateDto;
import zunik.ohimarket.service.PostCategoryService;
import zunik.ohimarket.service.PostService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {
    private final PostCategoryService postCategoryService;
    private final PostService postService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("categoriesMap", postCategoryService.findAllTransMap());

        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/createPost")
    public String createPostForm(Model model) {
        List<PostCategory> postCategories = postCategoryService.findAll();

        model.addAttribute("form", new PostCreateDto());
        model.addAttribute("postCategories", postCategories);

        return "createPost";
    }

    @PostMapping("/createPost")
    public String createPost(@Validated @ModelAttribute("form") PostCreateDto form,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "createPost";
        }

        Post post = new Post();
        post.setPostCategoryName(form.getPostCategoryName());
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setMemberId(loginMember.getId());
        postService.save(post);

        return "redirect:/";
    }
}
