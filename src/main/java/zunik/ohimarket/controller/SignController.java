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

    /**
     * 회원가입폼을 호출합니다.
     */
    @GetMapping("/signUp")
    public String signUpForm(Model model){
        model.addAttribute("form", new SignUpDto());
        return "sign/signUp";
    }

    /**
     * 새로운 사용자를 생성합니다.
     */
    @PostMapping("/signUp")
    public String singUp(@Validated @ModelAttribute("form") SignUpDto form, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "sign/signUp";
        }
        memberService.save(form);

        return "redirect:/signIn";
    }

    /**
     * 로그인폼을 호출합니다.
     */
    @GetMapping("/signIn")
    public String signInForm(Model model){
        model.addAttribute("form", new SignInDto());
        return "sign/signIn";
    }

    /**
     * 로그인 처리를 합니다.
     */
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
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:/";
    }

    /**
     * 로그아웃 합니다.
     */
    @PostMapping("/signOut")
    public String signOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/signIn";
    }
}
