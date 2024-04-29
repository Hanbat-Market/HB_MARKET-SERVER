package HM.Hanbat_Market.api.member;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.dto.FcmTokenRequest;
import HM.Hanbat_Market.api.member.dto.MemberRequestDto;
import HM.Hanbat_Market.api.member.dto.MemberResponseDto;
import HM.Hanbat_Market.api.member.dto.ProfileDetailRequest;
import HM.Hanbat_Market.api.member.dto.ProfileImageRequest;
import HM.Hanbat_Market.api.member.dto.ProfileImageResponse;
import HM.Hanbat_Market.api.member.dto.ProfileNicknameRequest;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.AlreadyLoginException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.member.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        Member member = Member.createMember(memberRequestDto.getMail(), memberRequestDto.getPasswd(),
                memberRequestDto.getNickname());
        memberService.join(member);

        return new Result(new MemberResponseDto(member.getMail()));
    }

    @PostMapping("/fcm/save")
    public Result saveFcmToken(@RequestBody FcmTokenRequest fcmTokenRequest, HttpServletRequest request) {

        Member member = memberRepository.findByUUID(fcmTokenRequest.getTargetMemberUuid()).get();

        String fcmToken = memberService.updateFcmToken(member.getId(), fcmTokenRequest.getFcmToken());

        return new Result("success save - " + fcmToken);
    }

    @PostMapping("/profiles/image")
    public Result saveProfileImage(@RequestPart(value = "imageFile", required = true) MultipartFile imageFile,
                                   @RequestPart("profileImageRequest") ProfileImageRequest profileImageRequest) throws IOException {

        ProfileImageResponse profileImageResponse = memberService.regisProfileImage(imageFile, profileImageRequest.getUuid());

        return new Result(profileImageResponse);
    }

    @GetMapping("/profiles")
    public Result getProfile(@RequestBody ProfileDetailRequest profileDetailRequest) {

        return new Result(memberService.getProfileDetail(profileDetailRequest.getUuid()));
    }

    @Transactional
    @PostMapping("/p")
    public String pp(@RequestBody ProfileDetailRequest profileDetailRequest) {

        Member member = memberRepository.findByUUID(profileDetailRequest.getUuid()).get();
        member.pp();
        memberRepository.save(member);

        return "ok";
    }

    @PutMapping("/profiles/nickname")
    public Result setNickname(@RequestBody ProfileNicknameRequest profileNicknameRequest) {

        memberService.setProfileNickname(profileNicknameRequest);

        return new Result(profileNicknameRequest.getNickName());
    }
}
