package HM.Hanbat_Market.repository.purchase;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    Long save(Purchase purchase);

    Optional<Purchase> findById(Long id);

    List<Purchase> findAll();
}
