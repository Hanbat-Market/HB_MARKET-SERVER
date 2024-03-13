package HM.Hanbat_Market.api.item;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.item.dto.PurchaseHistoryResponseDto;
import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;
import HM.Hanbat_Market.domain.entity.Trade;
import HM.Hanbat_Market.service.item.ItemService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import HM.Hanbat_Market.service.trade.TradeService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/purchaseHistory")
    public Result purchaseHistory(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
//        if (sessionMember == null) {
//            return new Result<>("로그인이 필요합니다");
//        }
        return new Result(itemService.purchaseHistoryToDto(sessionMember));
    }
}
