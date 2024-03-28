package HM.Hanbat_Market.api.trade.dto;


import HM.Hanbat_Market.domain.entity.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTradeIdResponseDto {
    String itemName;
    String sellerNickName;
    String purchaserNickname;
    Long tradeId;
    ItemStatus itemStatus;
}
