package HM.Hanbat_Market.service.item;

import HM.Hanbat_Market.api.item.dto.*;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import HM.Hanbat_Market.service.trade.TradeService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final PreemptionItemService preemptionItemService;
    private final TradeService tradeService;

    public MyPageResponseDto myPageToDto(Member loginMember) {
        try {
            List<Item> itemByMember = itemRepository.findAllByMember(loginMember);
            List<CompletedDto> completedDtos = new ArrayList<>();
            List<ReservedDto> reservedDtos = new ArrayList<>();
            for (Item item : itemByMember) {
                Trade trade = item.getTrade();
                if (trade == null) {
                    continue;
                }
                if (trade.getTradeStatus() == TradeStatus.RESERVATION) {
                    reservedDtos.add(new ReservedDto(loginMember, trade));
                } else if (item.getTrade().getTradeStatus() == TradeStatus.COMP) {
                    completedDtos.add(new CompletedDto(loginMember, trade));
                }
            }
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);

            return new MyPageResponseDto(preemptionItemByMember.size(), reservedDtos, completedDtos);
        } catch (NoResultException e) {
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);
            return new MyPageResponseDto(preemptionItemByMember.size(), null, null);
        }
    }

    public PurchaseHistoryResponseDto purchaseHistoryToDto(Member loginMember) {
        try {
            List<Trade> completedByMember = tradeService.findCompletedByMember(loginMember);
            List<Trade> reservedByMember = tradeService.findReservedByMember(loginMember);
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);

            List<ReservedDto> reservedDtos = reservedByMember.stream()
                    .map(r -> new ReservedDto(loginMember, r))
                    .collect(toList());

            List<CompletedDto> completedDtos = completedByMember.stream()
                    .map(c -> new CompletedDto(loginMember, c))
                    .collect(toList());


            return new PurchaseHistoryResponseDto(preemptionItemByMember.size(), reservedDtos, completedDtos);
        } catch (NoResultException e) {
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);
            return new PurchaseHistoryResponseDto(preemptionItemByMember.size(), null, null);
        }
    }

    public PreemptionItemsResult preemptionItemsResultToDto(Member loginMember) {
        try {
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);

            List<PreemptionItemDto> preemptionItemDtos = preemptionItemByMember.stream()
                    .map(p -> new PreemptionItemDto(p.getItem().getMember(), p))
                    .collect(toList());

            return new PreemptionItemsResult(preemptionItemByMember.size(), preemptionItemDtos);
        } catch (NoResultException e) {
            List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);
            return new PreemptionItemsResult(preemptionItemByMember.size(), null);
        }
    }
}
