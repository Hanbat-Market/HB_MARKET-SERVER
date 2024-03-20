package HM.Hanbat_Market.api.item.dto;

import HM.Hanbat_Market.domain.entity.ArticleStatus;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.Trade;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompletedDto {

    private Long id;

    private String seller;

    private String purchaser;

    private String title;

    private String description;

    private String tradingPlace;

    private ArticleStatus articleStatus;

    private String itemName;

    private Long price;

    private String thumbnailFilePath;

    private LocalDateTime createdAt;

    private LocalDateTime reservedDate;

    public CompletedDto(Member member, Trade trade, String thumbnailFilePath) {
        this.id = trade.getItem().getArticle().getId();
        this.seller = trade.getItem().getMember().getNickname();
        this.purchaser = trade.getMember().getNickname();
        this.title = trade.getItem().getArticle().getTitle();
        this.description = trade.getItem().getArticle().getDescription();
        this.tradingPlace = trade.getItem().getArticle().getTradingPlace();
        this.articleStatus = trade.getItem().getArticle().getArticleStatus();
        this.itemName = trade.getItem().getItemName();
        this.price = trade.getItem().getPrice();
        this.thumbnailFilePath = thumbnailFilePath;
        this.createdAt = trade.getItem().getArticle().getCreatedAt();
        this.reservedDate = trade.getTradeDate();
    }
}
