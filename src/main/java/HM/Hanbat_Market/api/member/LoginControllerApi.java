package HM.Hanbat_Market.api.member;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.dto.LoginRequestDto;
import HM.Hanbat_Market.api.member.dto.LogoutRequest;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.AlreadyLoginException;
import HM.Hanbat_Market.exception.member.LoginException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.member.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class LoginControllerApi {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @PostMapping("/logout")
    public Result logout(@RequestBody LogoutRequest logoutRequest, HttpServletRequest request) {

        Member member = memberRepository.findByUUID(logoutRequest.getUuid()).get();
        memberService.logout(member.getId());
        log.info("@@@@@@@@@@@ logout 상태 변경");

        return new Result("ok");
    }

}
