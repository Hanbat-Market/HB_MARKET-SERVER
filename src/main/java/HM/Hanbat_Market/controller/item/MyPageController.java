package HM.Hanbat_Market.controller.item;

import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import HM.Hanbat_Market.service.trade.TradeService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final TradeService tradeService;
    private final PreemptionItemService preemptionItemService;
    private final ItemRepository itemRepository;

    @GetMapping("/mypage")
    public String myPage(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                    required = false) Member loginMember, Model model
    ) {
        try {
            List<Item> allByMember = itemRepository.findAllByMember(loginMember);
            List<Trade> completedByMember = new ArrayList<>();
            List<Trade> reservedByMember = new ArrayList<>();
            for (Item item : allByMember) {
                if (item.getTrade() == null) {
                    continue;
                }
                if (item.getTrade().getTradeStatus() == TradeStatus.RESERVATION) {
                    reservedByMember.add(item.getTrade());
                } else if (item.getTrade().getTradeStatus() == TradeStatus.COMP) {
                    completedByMember.add(item.getTrade());
                }
            }
            model.addAttribute("member", loginMember);
            model.addAttribute("completedByMember", completedByMember);
            model.addAttribute("reservedByMember", reservedByMember);
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);
            model.addAttribute("memberPreemptionSize", preemptionItemByMember.size());
            return "mypage";
        } catch (NoResultException e) {
            return "mypage";
        }
    }
}
