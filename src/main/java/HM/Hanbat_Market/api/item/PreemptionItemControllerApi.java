package HM.Hanbat_Market.api.item;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.item.dto.*;
import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.item.PreemptionItemRepository;
import HM.Hanbat_Market.service.item.ItemService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import HM.Hanbat_Market.service.trade.TradeService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class PreemptionItemControllerApi {

    private final ItemRepository itemRepository;
    private final PreemptionItemService preemptionItemService;
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/preemption/{itemId}")
    public Result preemption(@PathVariable("itemId") Long itemId,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
//        if (sessionMember == null) {
//            return new Result<>("로그인이 필요합니다");
//        }
        Item item = itemRepository.findById(itemId).get();

        try {
            PreemptionItem preemptionItem = preemptionItemService.findPreemptionItemByMemberAndItem(sessionMember, item);
            Long preemptionItemId = preemptionItem.getId();

            if (preemptionItem.getPreemptionItemStatus() == PreemptionItemStatus.CANCEL) {
                preemptionItemService.activePreemption(preemptionItemId);
            } else if (preemptionItem.getPreemptionItemStatus() == PreemptionItemStatus.PREEMPTION) {
                preemptionItemService.cancelPreemption(preemptionItemId);
            }
            return new Result("Toggle ok " + preemptionItem.getPreemptionItemStatus());
        } catch (NoResultException e) {
            preemptionItemService.regisPreemption(sessionMember.getId(), itemId);
            return new Result("Regis ok");
        }
    }

    @GetMapping("/preemptionItems")
    public Result myPage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
//        if (sessionMember == null) {
//            return new Result<>("로그인이 필요합니다");
//        }
        return new Result(itemService.preemptionItemsResultToDto(sessionMember));
    }
}
