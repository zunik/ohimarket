package zunik.ohimarket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final MemberService memberService;

    @GetMapping("/myProfile")
    public String profile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("loginMember");
        log.info("member = {}", member);
        model.addAttribute("member", member);
        return "profile";
    }

//    @GetMapping("/myProfile")
    public String profileTest(Model model) {
        Member member = memberService.getByEmail("chazunik@gmail.com");
        model.addAttribute("member", member);
        return "profile";
    }

}