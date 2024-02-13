package HM.Hanbat_Market.controller.item;

import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;
import HM.Hanbat_Market.domain.entity.Trade;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import HM.Hanbat_Market.service.trade.TradeService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PurchaseHistoryController {

    private final TradeService tradeService;
    private final PreemptionItemService preemptionItemService;

    @GetMapping("/purchaseHistory")
    public String purchaseHistory(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                    required = false) Member loginMember, Model model
    ) {
        try {
            List<Trade> completedByMember = tradeService.findCompletedByMember(loginMember);
            List<Trade> reservedByMember = tradeService.findReservedByMember(loginMember);
            model.addAttribute("member", loginMember);
            model.addAttribute("completedByMember", completedByMember);
            model.addAttribute("reservedByMember", reservedByMember);
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);
            model.addAttribute("memberPreemptionSize", preemptionItemByMember.size());
            return "purchaseHistory";
        } catch (NoResultException e) {
            return "purchaseHistory";
        }
    }
}
