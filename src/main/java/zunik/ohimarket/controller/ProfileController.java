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
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.controller.dto.MemberUpdateDto;
import zunik.ohimarket.service.PostCategoryService;
import zunik.ohimarket.service.dto.ProfileResponseDto;
import zunik.ohimarket.service.MemberService;
import zunik.ohimarket.service.ProfileService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final MemberService memberService;
    private final ProfileService profileService;
    private final PostCategoryService postCategoryService;

    /**
     * 사용자의 상세정보를 보여줍니다.
     */
    @GetMapping("/profile/{memberToken}")
    public String profileDetail(
            @PathVariable String memberToken,
            Model model) {
        ProfileResponseDto profileResponseDto = profileService.getDetail(memberToken);

        model.addAttribute("categoriesMap", postCategoryService.findAllTransMap());
        model.addAttribute("profile", profileResponseDto);
        return "profile/detailView";
    }

    /**
     * 사용자의 프로필의 수정폼을 호출합니다.
     * 단, 자신의 프로필만 수정할 수 있습니다.
     */
    @GetMapping("/myProfile/edit")
    public String editForm(Model model,
                                    @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Member member = memberService.findById(loginMember.getId()).get();

        MemberUpdateDto form = new MemberUpdateDto();

        form.setNickname(member.getNickname());
        form.setIntroduction(member.getIntroduction());

        model.addAttribute("form", form);
        return "profile/editForm";
    }

    /**
     * 사용자의 프로필을 수정합니다.
     */
    @PostMapping("/myProfile/edit")
    public String editMyProfile(
            @Validated @ModelAttribute("form") MemberUpdateDto form,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "profile/editForm";
        }

        memberService.update(
                loginMember.getId(),
                form);

        redirectAttributes.addAttribute("memberToken", loginMember.getToken());
        return "redirect:/profile/{memberToken}";
    }

}
