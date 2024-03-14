package HM.Hanbat_Market.api.member;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.dto.MemberRequestDto;
import HM.Hanbat_Market.api.member.dto.MemberResponseDto;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.AlreadyLoginException;
import HM.Hanbat_Market.service.member.MemberService;
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

    @PostMapping("/members/new")
    public Result create(@RequestBody @Valid MemberRequestDto memberRequestDto, BindingResult result,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {

        if (sessionMember != null) {
            throw new AlreadyLoginException();
        }

        Member member = Member.createMember(memberRequestDto.getMail(), memberRequestDto.getPasswd(), memberRequestDto.getMail(), memberRequestDto.getNickname());
        memberService.join(member);
        return new Result(new MemberResponseDto(member.getMail()));
    }
}
