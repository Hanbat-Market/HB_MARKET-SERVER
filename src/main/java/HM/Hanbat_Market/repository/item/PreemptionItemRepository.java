package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;

import java.util.List;
import java.util.Optional;

public interface PreemptionItemRepository {
    PreemptionItem save(PreemptionItem preemptionItem);

    Optional<PreemptionItem> findById(Long id);

    PreemptionItem findByMemberAndItem(Member member, Item item);

    List<PreemptionItem> findAllByMember(Member member);

    List<PreemptionItem> findAllByItem(Item item);

    List<PreemptionItem> findAll();
}
