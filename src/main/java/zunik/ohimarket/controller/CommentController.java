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
import zunik.ohimarket.controller.dto.CommentUpdateDto;
import zunik.ohimarket.domain.Comment;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.controller.dto.CommentCreateDto;
import zunik.ohimarket.exception.AccessDeniedException;
import zunik.ohimarket.service.CommentService;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글을 생성합니다.
     */
    @PostMapping("/add")
    public String addComment(
            @ModelAttribute CommentCreateDto form,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            RedirectAttributes redirectAttributes
    ) {
        String postToken = form.getPostToken();
        commentService.save(form, loginMember);

        redirectAttributes.addAttribute("postToken", postToken);
        return "redirect:/post/{postToken}#commentBox";
    }

    /**
     * 댓글 수정폼을 호출합니다.
     */
    @GetMapping("/{commentToken}/edit")
    public String editForm(
            @PathVariable String commentToken,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model
    ) {
        Comment comment = commentService.findByToken(commentToken);

        if (comment.getMember().getId() != loginMember.getId()) {
            // 행위자에게 권한이 없을 경우
            throw new AccessDeniedException();
        }

        CommentUpdateDto form = new CommentUpdateDto();
        form.setContent(comment.getContent());

        model.addAttribute("form", form);
        return "comment/editForm";
    }

    /**
     * 댓글을 수정합니다.
     */
    @PostMapping("/{commentToken}/edit")
    public String editComment(
            @PathVariable String commentToken,
            @Validated @ModelAttribute("form") CommentUpdateDto form,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "post/editForm";
        }

        String postToken = commentService.update(
                commentToken, form, loginMember.getId()
        );

        redirectAttributes.addAttribute("postToken", postToken);
        redirectAttributes.addAttribute("commentToken", commentToken);
        return "redirect:/post/{postToken}#{commentToken}";
    }

    /**
     * 댓글을 삭제합니다.
     */
    @PostMapping("/delete")
    public String deleteComment(
            @RequestParam(value = "commentToken") String commentToken,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            RedirectAttributes redirectAttributes
    ) {
        String postToken = commentService.delete(commentToken, loginMember.getId());

        redirectAttributes.addAttribute("postToken", postToken);
        return "redirect:/post/{postToken}#commentBox";
    }
}
