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
import zunik.ohimarket.dto.MemberUpdateDto;
import zunik.ohimarket.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final MemberService memberService;

    @GetMapping("/myProfile")
    public String myProfile(Model model,
                          @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Member member = memberService.findById(loginMember.getId()).get();
        model.addAttribute("member", member);
        return "profile";
    }

//    @GetMapping("/myProfile")
    public String myProfileTest(Model model) {
        Member member = memberService.findByEmail("chazunik@gmail.com");
        model.addAttribute("member", member);
        return "profile";
    }

    @GetMapping("/editProfile")
    public String editMyProfileForm(Model model,
                                    @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Member member = memberService.findById(loginMember.getId()).get();

        MemberUpdateDto form = new MemberUpdateDto();

        form.setNickname(member.getNickname());
        form.setIntroduction(member.getIntroduction());

        model.addAttribute("form", form);
        return "editProfile";
    }

    @PostMapping("/editProfile")
    public String editMyProfile(@Validated @ModelAttribute("form") MemberUpdateDto form,
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "editProfile";
        }

        memberService.update(
                loginMember.getId(),
                form);

        return "redirect:/myProfile";
    }

}
