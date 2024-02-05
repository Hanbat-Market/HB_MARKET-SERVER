package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPreemptionItemRepository implements PreemptionItemRepository{

    private final EntityManager em;

    @Override
    public PreemptionItem save(PreemptionItem preemptionItem) {
        //        if (item.getId() == null){
        //            em.persist(item);
        //        }else {
        //            em.merge(item);
        //        }
        em.persist(preemptionItem);
        return preemptionItem;
    }

    @Override
    public Optional<PreemptionItem> findById(Long id) {
        PreemptionItem preemptionItem = em.find(PreemptionItem.class, id);
        return Optional.ofNullable(preemptionItem);
    }

    @Override
    public List<PreemptionItem> findAllByMember(Member member) {
        Long memberId = member.getId();
        return em.createQuery("select p from PreemptionItem p join p.member m where m.id = :memberId")
                .setParameter("memberId", member.getId())
                .getResultList();
    }

    @Override
    public List<PreemptionItem> findAllByItem(Item item) {
        Long itemId = item.getId();
        return em.createQuery("select p from PreemptionItem p join p.item i where i.id = :itemId")
                .setParameter("itemId", item.getId())
                .getResultList();
    }

    @Override
    public List<PreemptionItem> findAll() {
        return em.createQuery("select p from PreemptionItem p")
                .getResultList();
    }
}
