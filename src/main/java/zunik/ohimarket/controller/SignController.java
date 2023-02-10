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
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.dto.SignInDto;
import zunik.ohimarket.dto.SignUpDto;
import zunik.ohimarket.service.MemberService;
import zunik.ohimarket.service.SignInService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SignController {
    private final MemberService memberService;
    private final SignInService signInService;

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("form", new SignUpDto());
        return "signUp";
    }

    @PostMapping("/sign-up")
    public String singUp(@Validated @ModelAttribute("form") SignUpDto form, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "signUp";
        }

        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setNickname(form.getNickname());
        member.setPassword(form.getPassword());
        memberService.save(member);

        return "redirect:/sign-in";
    }

    @GetMapping("/sign-in")
    public String signInForm(Model model){
        model.addAttribute("form", new SignInDto());
        return "signIn";
    }

    @PostMapping("/sign-in")
    public String signIn(@Validated @ModelAttribute("form") SignInDto form,
                         BindingResult bindingResult, HttpServletRequest request,
                         Model model) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "signIn";
        }

        // 인증 체크
        boolean authCheck = signInService.authCheck(form.getEmail(), form.getPassword());

        if (!authCheck) {
            // TODO 로그인 실패시 메시지 띄우기
            model.addAttribute("loginFail", "아이디 또는 패스워드를 확인해주세요");
            return "signIn";
        }

        Member member = memberService.getByEmail(form.getEmail());

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member);

        return "redirect:/";
    }
}
