package HM.Hanbat_Market.api.item;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.service.item.ItemService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import HM.Hanbat_Market.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class MyPageControllerApi {
    private final TradeService tradeService;
    private final PreemptionItemService preemptionItemService;
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/mypage")
    public Result myPage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
//        if (sessionMember == null) {
//            return new Result<>("로그인이 필요합니다");
//        }

        return new Result(itemService.myPageToDto(sessionMember));
    }
}
