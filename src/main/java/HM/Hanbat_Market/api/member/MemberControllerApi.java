package HM.Hanbat_Market.api.member;

import HM.Hanbat_Market.api.member.dto.MemberRequestDto;
import HM.Hanbat_Market.api.member.dto.MemberResponseDto;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class MemberControllerApi {

    private final MemberService memberService;

    @PostMapping("/members/new")
    public MemberResponseDto create(@RequestBody @Valid MemberRequestDto memberRequestDto, BindingResult result) {
        Member member = Member.createMember(memberRequestDto.getMail(), memberRequestDto.getPasswd(), memberRequestDto.getMail(), memberRequestDto.getNickname());
        memberService.join(member);
        return new MemberResponseDto(member.getMail());
    }
}
