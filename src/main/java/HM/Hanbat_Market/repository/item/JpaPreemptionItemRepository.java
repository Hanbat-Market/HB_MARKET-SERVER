package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPreemptionItemRepository implements PreemptionItemRepository {

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
    public PreemptionItem findByMemberAndItem(Member member, Item item) throws NoResultException {
        Long memberId = member.getId();
        Long itemId = item.getId();
        return em.createQuery("select p from PreemptionItem p join fetch p.member m join p.item i where m.id = :memberId and i.id = :itemId" +
                        " and p.item.itemStatus = :itemStatus", PreemptionItem.class)
                .setParameter("memberId", member.getId())
                .setParameter("itemStatus", ItemStatus.SALE)
                .setParameter("itemId", item.getId())
                .getSingleResult();
    }

    @Override
    public List<PreemptionItem> findAllByMember(Member member) {
        Long memberId = member.getId();
        return em.createQuery("select p from PreemptionItem p join fetch p.member m where m.id = :memberId" +
                        " and p.preemptionItemStatus = :preemptionItemStatus", PreemptionItem.class)
                .setParameter("memberId", member.getId())
                .setParameter("preemptionItemStatus", PreemptionItemStatus.PREEMPTION)
                .getResultList();
    }

    @Override
    public List<PreemptionItem> findAllByItem(Item item) {
        Long itemId = item.getId();
        return em.createQuery("select p from PreemptionItem p join fetch p.item i where i.id = :itemId" +
                        " and p.preemptionItemStatus = :preemptionItemStatus", PreemptionItem.class)
                .setParameter("itemId", item.getId())
                .setParameter("preemptionItemStatus", PreemptionItemStatus.PREEMPTION)
                .getResultList();
    }

    @Override
    public List<PreemptionItem> findAll() {
        return em.createQuery("select p from PreemptionItem p where p.preemptionItemStatus != :preemptionItemStatus")
                .setParameter("preemptionItemStatus", PreemptionItemStatus.CANCEL)
                .getResultList();
    }
}
