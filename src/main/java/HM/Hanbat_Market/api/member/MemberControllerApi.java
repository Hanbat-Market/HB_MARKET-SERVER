package HM.Hanbat_Market.api.member;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.dto.FcmTokenRequest;
import HM.Hanbat_Market.api.member.dto.LogoutRequest;
import HM.Hanbat_Market.api.member.dto.MemberRequestDto;
import HM.Hanbat_Market.api.member.dto.MemberResponseDto;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.AlreadyLoginException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.member.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class MemberControllerApi {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Hidden
    @PostMapping("/members/new")
    public Result create(@RequestBody MemberRequestDto memberRequestDto,
                         HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        if (sessionMember != null) {
            throw new AlreadyLoginException();
        }

        Member member = Member.createMember(memberRequestDto.getMail(), memberRequestDto.getPasswd(), memberRequestDto.getNickname());
        memberService.join(member);

        return new Result(new MemberResponseDto(member.getMail()));
    }

    @PostMapping("/fcm/save")
    public Result saveFcmToken(@RequestBody FcmTokenRequest fcmTokenRequest, HttpServletRequest request) {

        Member member = memberRepository.findByUUID(fcmTokenRequest.getTargetMemberUuid()).get();

        String fcmToken = memberService.updateFcmToken(member.getId(), fcmTokenRequest.getFcmToken());

        return new Result("success save - " + fcmToken);
    }
}
