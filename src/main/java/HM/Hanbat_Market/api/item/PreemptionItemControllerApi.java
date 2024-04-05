package HM.Hanbat_Market.api.item;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.item.ItemService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class PreemptionItemControllerApi {

    private final ItemRepository itemRepository;
    private final PreemptionItemService preemptionItemService;
    private final ItemService itemService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @PostMapping("/preemption/{itemId}")
    public Result preemption(@PathVariable("itemId") Long itemId,
                             HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

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
        } catch (NullPointerException e) {
            preemptionItemService.regisPreemption(sessionMember.getId(), itemId);
            return new Result("Regis ok");
        }
    }


    @GetMapping("/preemptionItems")
    public Result preemptionItems(HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        return new Result(itemService.preemptionItemsResultToDto(sessionMember));
    }
}
