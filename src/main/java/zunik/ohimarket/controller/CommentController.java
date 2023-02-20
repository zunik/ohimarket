package zunik.ohimarket.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zunik.ohimarket.constant.SessionConst;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.controller.dto.CommentCreateDto;
import zunik.ohimarket.service.CommentService;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add")
    public String addComment(
            @ModelAttribute CommentCreateDto form,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            RedirectAttributes redirectAttributes
    ) {
        Long postId = form.getPostId();

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setMember(loginMember);
        comment.setContent(form.getContent());

        commentService.save(comment);

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}#commentBox";
    }

    @PostMapping("/delete")
    public String deletePost(
            @RequestParam(value = "commentToken") String commentToken,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            RedirectAttributes redirectAttributes
    ) {
        Long postId = commentService.delete(commentToken, loginMember.getId());

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}#commentBox";
    }
}
