package HM.Hanbat_Market.verification;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.account.NoHanbatMailException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.verification.dto.IsVerifiedRequest;
import HM.Hanbat_Market.verification.dto.MailRequest;
import HM.Hanbat_Market.verification.dto.MailResponse;
import HM.Hanbat_Market.verification.dto.VerificationRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VerificationController {

    private final MailService mailService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @ResponseBody
    @PostMapping("/verification")
    public Result MailSend(@RequestBody MailRequest mailRequest) {
        // 이메일 주소 유효성 검사를 위한 정규식 패턴
        String emailPattern = "^[a-zA-Z0-9._%+-]+@(o365\\.hanbat\\.ac\\.kr|edu\\.hanbat\\.ac\\.kr)$";

        // 입력된 이메일 주소가 패턴과 일치하는지 확인
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(mailRequest.getMail());
        if (!matcher.matches()) {
            // 유효하지 않은 이메일 형식일 경우 처리
            throw new NoHanbatMailException();
        }

        // 이메일 전송 및 인증번호 발급
        int number = mailService.sendMail(mailRequest.getMail());
        String num = String.valueOf(number);
        memberService.regisVerificationNumber(mailRequest.getMemberUuid(), num);

        return new Result<>("success");
    }


    @ResponseBody
    @PostMapping("/verification/match")
    public Result matchingNumber(@RequestBody VerificationRequest verificationRequest) {

        memberService.verification(verificationRequest.getMemberUuid(), verificationRequest.getNumber());

        return new Result("success");
    }

    @ResponseBody
    @PostMapping("/verification/confirm")
    public Result isVerifiedStudent(@RequestBody IsVerifiedRequest isVerifiedRequest) {

        if (memberService.isVerifiedStudent(isVerifiedRequest.getMemberUuid())) {
            return new Result("verified");
        }

        return new Result("uuid를 올바르게 넣어주세요");
    }
}