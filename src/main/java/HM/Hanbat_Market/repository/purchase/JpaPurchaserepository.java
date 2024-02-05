package HM.Hanbat_Market.repository.purchase;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Purchase;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPurchaserepository implements PurchaseRepository {

    private final EntityManager em;

    @Override
    public Long save(Purchase purchase) {
        em.persist(purchase);
        return purchase.getId();
    }

    @Override
    public Optional<Purchase> findById(Long id) {
        return Optional.ofNullable(em.find(Purchase.class, id));
    }

    //추후에 동적쿼리로 원하는 구매내역만을 볼 수 있는 기능 추가예정
    @Override
    public List<Purchase> findAll() {
        return em.createQuery("select p from Purchase p")
                .getResultList();
    }
}
