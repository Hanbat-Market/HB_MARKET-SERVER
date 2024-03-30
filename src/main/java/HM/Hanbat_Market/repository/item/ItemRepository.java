package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    Optional<Item> findById(Long id);

    Item findByItemNameAndSellerName(String itemName, String sellerName);

    List<Item> findAllByMember(Member member);

    Item findAllByArticle(Article article);

    List<Item> findAll();
}
