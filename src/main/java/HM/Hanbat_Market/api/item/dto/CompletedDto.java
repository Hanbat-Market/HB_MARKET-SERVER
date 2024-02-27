package HM.Hanbat_Market.api.item.dto;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.Trade;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompletedDto {
    private String seller;

    private String purchaser;

    private String title;

    private String itemName;

    private Long price;

    private LocalDateTime tradeDate;
    public CompletedDto(Member member, Trade trade){
        this.seller = member.getNickname();
        this.purchaser = trade.getMember().getNickname();
        this.title = trade.getItem().getArticle().getTitle();
        this.itemName = trade.getItem().getItemName();
        this.price = trade.getItem().getPrice();
        this.tradeDate = trade.getTradeDate();
    }
}
