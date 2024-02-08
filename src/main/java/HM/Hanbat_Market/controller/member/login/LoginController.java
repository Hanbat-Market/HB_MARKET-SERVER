package HM.Hanbat_Market.controller.member.login;

import HM.Hanbat_Market.controller.member.dto.LoginForm;
import HM.Hanbat_Market.controller.member.dto.MemberForm;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form
            , @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        if (sessionMember != null) {
            return "redirect:/";
        }
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult result
            , HttpServletRequest request, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember
            , @RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL) {
        if (sessionMember != null) {
            return "redirect:/";
        }
        if (result.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = memberService.login(form.getMail(), form.getPasswd());

        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
