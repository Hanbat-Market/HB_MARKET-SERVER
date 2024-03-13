package HM.Hanbat_Market.api.item.dto;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreemptionItemDto {
    private String seller;

    private String title;

    private String itemName;

    private Long price;

    public PreemptionItemDto(Member member, PreemptionItem preemptionItem){
        this.seller = member.getNickname();
        this.title = preemptionItem.getItem().getArticle().getTitle();
        this.itemName = preemptionItem.getItem().getItemName();
        this.price = preemptionItem.getItem().getPrice();
    }
}
