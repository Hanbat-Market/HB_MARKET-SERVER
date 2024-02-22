package HM.Hanbat_Market.api.article.dto;

import HM.Hanbat_Market.domain.entity.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleSearchDto {
    ItemStatus itemStatus;
    String title;
    String itemName;
}
