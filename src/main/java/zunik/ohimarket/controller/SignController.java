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
import zunik.ohimarket.constant.SessionConst;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.controller.dto.SignInDto;
import zunik.ohimarket.controller.dto.SignUpDto;
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

    @GetMapping("/signUp")
    public String signUpForm(Model model){
        model.addAttribute("form", new SignUpDto());
        return "sign/signUp";
    }

    @PostMapping("/signUp")
    public String singUp(@Validated @ModelAttribute("form") SignUpDto form, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "sign/signUp";
        }
        memberService.save(form);

        return "redirect:/signIn";
    }

    @GetMapping("/signIn")
    public String signInForm(Model model){
        model.addAttribute("form", new SignInDto());
        return "sign/signIn";
    }

    @PostMapping("/signIn")
    public String signIn(@Validated @ModelAttribute("form") SignInDto form,
                         BindingResult bindingResult, HttpServletRequest request,
                         Model model) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "sign/signIn";
        }

        // 인증 체크
        boolean authCheck = signInService.authCheck(form.getEmail(), form.getPassword());

        if (!authCheck) {
            // TODO 로그인 실패시 메시지 띄우기
            model.addAttribute("loginFail", "아이디 또는 패스워드를 확인해주세요");
            return "sign/signIn";
        }

        Member member = memberService.findByEmail(form.getEmail());

        HttpSession session = request.getSession();
        // TODO 세션에 Member 객체 말고 필요한 정보만 넣기
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:/";
    }

    @PostMapping("/signOut")
    public String signOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/signIn";
    }
}
