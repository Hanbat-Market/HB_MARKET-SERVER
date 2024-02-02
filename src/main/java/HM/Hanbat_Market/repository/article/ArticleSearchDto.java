package HM.Hanbat_Market.repository.article;

import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleSearchDto {
    ItemStatus itemStatus;

    String itemName;
}
