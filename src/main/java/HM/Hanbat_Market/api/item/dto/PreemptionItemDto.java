package HM.Hanbat_Market.api.item.dto;

import HM.Hanbat_Market.domain.entity.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PreemptionItemDto {
    private String uuid;

    private Long id;

    private String seller;

    private String title;

    private String description;

    private String tradingPlace;

    private ArticleStatus articleStatus;

    private String itemName;

    private Long price;

    private String thumbnailFilePath;

    private LocalDateTime createdAt;

    private ItemStatus itemStatus;

    private int preemptionSize;

    private PreemptionItemStatus preemptionItemStatus;

    public PreemptionItemDto(Member member, PreemptionItem preemptionItem, String thumbnailFilePath, int preemptionSize, PreemptionItemStatus preemptionItemStatus) {
        this.uuid = member.getUuid();
        this.id = preemptionItem.getItem().getArticle().getId();
        this.seller = member.getNickname();
        this.title = preemptionItem.getItem().getArticle().getTitle();
        this.description = preemptionItem.getItem().getArticle().getDescription();
        this.tradingPlace = preemptionItem.getItem().getArticle().getTradingPlace();
        this.articleStatus = preemptionItem.getItem().getArticle().getArticleStatus();
        this.itemName = preemptionItem.getItem().getItemName();
        this.price = preemptionItem.getItem().getPrice();
        this.thumbnailFilePath = thumbnailFilePath;
        this.createdAt = preemptionItem.getItem().getArticle().getCreatedAt();
        this.itemStatus = preemptionItem.getItem().getItemStatus();
        this.preemptionSize = preemptionSize;
        this.preemptionItemStatus = preemptionItemStatus;
    }
}
