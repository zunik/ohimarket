package zunik.ohimarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import zunik.ohimarket.domain.Post;
import zunik.ohimarket.service.PostCategoryService;
import zunik.ohimarket.service.PostService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final PostCategoryService postCategoryService;
    private final PostService postService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("categoriesMap", postCategoryService.findAllTransMap());

        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }
}
