package HM.Hanbat_Market.api.item;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.item.ItemService;
import HM.Hanbat_Market.service.member.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class PurchaseHistoryControllerApi {

    private final MemberService memberService;
    private final ItemService itemService;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @GetMapping("/purchaseHistory")
    public Result purchaseHistory(@Parameter(hidden = true) HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        return new Result(itemService.purchaseHistoryToDto(sessionMember));
    }
}
