package HM.Hanbat_Market.api.dto;

import HM.Hanbat_Market.domain.entity.ArticleStatus;
import HM.Hanbat_Market.domain.entity.ItemStatus;
import HM.Hanbat_Market.domain.entity.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
public class HomeArticlesDto {
    private Long id;

    private String title;

    private String description;

    private String tradingPlace;

    private ArticleStatus articleStatus;

    private String itemName;

    private Long price;

    private String memberNickname;

    private String thumbnailFilePath;

    private LocalDateTime createdAt;

    private int preemptionSize;

    private ItemStatus itemStatus;

}
