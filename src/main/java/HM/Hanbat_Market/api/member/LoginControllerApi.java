package HM.Hanbat_Market.api.member;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.controller.member.dto.LoginForm;
import HM.Hanbat_Market.controller.member.login.SessionConst;
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

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class LoginControllerApi {

    private final MemberService memberService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm form, BindingResult result
            , HttpServletRequest request, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember
            , @RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL) {

        if (sessionMember != null) {
            return new Result<>("이미 로그인 되어있음");
        }

        if (result.hasErrors()) {
            return new Result<>(result.getFieldError().toString());
        }

        Member loginMember = memberService.login(form.getMail(), form.getPasswd());

        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return new Result<>(result.getFieldError().toString());
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return new Result<>("ok");
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "ok";
    }
}
