package HM.Hanbat_Market.controller.item;

import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.item.PreemptionItemRepository;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import HM.Hanbat_Market.service.trade.TradeService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PrinterAbortException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PreemptionItemController {

    private final ItemRepository itemRepository;
    private final PreemptionItemService preemptionItemService;
    private final PreemptionItemRepository preemptionItemRepository;
    private final TradeService tradeService;

    @GetMapping("/preemption/{itemId}")
    public String preemption(@PathVariable("itemId") Long itemId,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                                     required = false) Member loginMember, Model model
    ) {
        Item item = itemRepository.findById(itemId).get();
        try {
            PreemptionItem preemptionItem = preemptionItemService.findPreemptionItemByMemberAndItem(loginMember, item);
            preemptionItemService.activePreemption(preemptionItem.getId());
            return "redirect:/";
        } catch (NoResultException e) {
            Long preemption = preemptionItemService.regisPreemption(loginMember.getId(), itemId);
            return "redirect:/";
        }
    }

    @GetMapping("/preemptionFromDetail/{itemId}")
    public String preemptionFromDetail(@PathVariable("itemId") Long itemId,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                                     required = false) Member loginMember, Model model
    ) {
        Item item = itemRepository.findById(itemId).get();
        try {
            PreemptionItem preemptionItem = preemptionItemService.findPreemptionItemByMemberAndItem(loginMember, item);
            preemptionItemService.activePreemption(preemptionItem.getId());
            Long articleId = item.getArticle().getId();
            return "redirect:/articles/"+articleId;
        } catch (NoResultException e) {
            Long preemption = preemptionItemService.regisPreemption(loginMember.getId(), itemId);
            Long articleId = item.getArticle().getId();
            return "redirect:/articles/"+articleId;
        }
    }


    @GetMapping("/preemption/mypage/{preemptionItemId}")
    public String preemptionMyPage(@PathVariable("preemptionItemId") Long preemptionItemId,
                                   @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                                           required = false) Member loginMember, Model model
    ) {
        try {
            PreemptionItem preemptionItem = preemptionItemRepository.findById(preemptionItemId).get();
            preemptionItemService.cancelPreemption(preemptionItem.getId());
            return "redirect:/preemption";
        } catch (NoResultException e) {
            return "redirect:/preemption";
        }
    }

    @GetMapping("/preemption")
    public String myPage(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                    required = false) Member loginMember, Model model
    ) {
        try {
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);
            List<Trade> completedByMember = tradeService.findCompletedByMember(loginMember);
            List<Trade> reservedByMember = tradeService.findReservedByMember(loginMember);
            model.addAttribute("memberPreemptionSize", preemptionItemByMember.size());
            model.addAttribute("preemptionItemByMember", preemptionItemByMember);
            model.addAttribute("member", loginMember);
            return "preemption";
        } catch (NoResultException e) {
            return "preemption";
        }
    }
}
