package zunik.ohimarket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zunik.ohimarket.constant.SessionConst;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.service.PostLikeService;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/like")
    public String like(
            @RequestParam(value = "postToken") String postToken,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            RedirectAttributes redirectAttributes
    ) {
        postLikeService.LikeToggle(
                postToken,
                loginMember.getId());

        redirectAttributes.addAttribute("postToken", postToken);
        return "redirect:/post/{postToken}#likeButton";
    }
}
