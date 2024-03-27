package HM.Hanbat_Market.service.preemption;

import HM.Hanbat_Market.api.item.dto.CompletedDto;
import HM.Hanbat_Market.api.item.dto.PreemptionItemsResult;
import HM.Hanbat_Market.api.item.dto.PurchaseHistoryResponseDto;
import HM.Hanbat_Market.api.item.dto.ReservedDto;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.item.PreemptionItemRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PreemptionItemService {

    private final PreemptionItemRepository preemptionItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long regisPreemption(Long memberId, Long itemId) {
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findById(itemId).get();
        PreemptionItem preemptionItem = PreemptionItem.createPreemptionItem(member, item);

        preemptionItemRepository.save(preemptionItem);

        return preemptionItem.getId();
    }

    //판매자가 판매를 완료하거나 구매자를 확실히 결정하면 구매완료
    @Transactional
    public Long cancelPreemption(Long preemptionItemId) {
        PreemptionItem preemptionItem = preemptionItemRepository.findById(preemptionItemId).get();
        preemptionItem.cancel();
        return preemptionItemId;
    }

    @Transactional
    public Long activePreemption(Long preemptionItemId) {
        PreemptionItem preemptionItem = preemptionItemRepository.findById(preemptionItemId).get();
        preemptionItem.active();
        return preemptionItemId;
    }

    //회원별 관심상품 조회
    public List<PreemptionItem> findPreemptionItemByMember(Member member) {
        return preemptionItemRepository.findAllByMember(member);
    }

    //아이템별 관심상품 조회
    public List<PreemptionItem> findPreemptionItemByItem(Item item) {
        return preemptionItemRepository.findAllByItem(item);
    }

    public PreemptionItem findPreemptionItemByMemberAndItem(Member member, Item item) {
        return preemptionItemRepository.findByMemberAndItem(member, item);
    }

    //전체조회
    public List<PreemptionItem> findAllPreemptionItem() {
        return preemptionItemRepository.findAll();
    }
}
