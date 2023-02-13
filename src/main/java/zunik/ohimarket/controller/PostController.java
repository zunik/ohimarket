package zunik.ohimarket.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import zunik.ohimarket.domain.PostCategory;
import zunik.ohimarket.dto.PostCreateDto;
import zunik.ohimarket.service.PostCategoryService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {
    private final PostCategoryService postCategoryService;

    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/createPost")
    public String createPostForm(Model model) {
        List<PostCategory> postCategories = postCategoryService.findAll();

        model.addAttribute("form", new PostCreateDto());
        model.addAttribute("postCategories", postCategories);

        return "createPost";
    }
}
