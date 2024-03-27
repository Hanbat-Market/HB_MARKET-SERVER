package HM.Hanbat_Market.api.item.dto;

import HM.Hanbat_Market.domain.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompletedDto {

    private Long id;

    private Long tradeId;

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

    private ItemStatus itemStatus;

    private int preemptionSize;

    private PreemptionItemStatus preemptionItemStatus;

    public CompletedDto(Member member, Item item, String thumbnailFilePath, int preemptionSize, PreemptionItemStatus preemptionItemStatus) {
        this.id = item.getArticle().getId();
        this.tradeId = item.getTrade().getId();
        this.seller = item.getMember().getNickname();
        this.purchaser = item.getTrade().getMember().getNickname();
        this.title = item.getArticle().getTitle();
        this.description = item.getArticle().getDescription();
        this.tradingPlace = item.getArticle().getTradingPlace();
        this.articleStatus = item.getArticle().getArticleStatus();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.thumbnailFilePath = thumbnailFilePath;
        this.createdAt = item.getArticle().getCreatedAt();
        this.reservedDate = item.getTrade().getReservationDate();
        this.itemStatus = item.getItemStatus();
        this.preemptionSize = preemptionSize;
        this.preemptionItemStatus = preemptionItemStatus;
    }
}
