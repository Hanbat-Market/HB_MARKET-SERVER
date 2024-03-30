package HM.Hanbat_Market.api.trade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetTradeIdRequestDto {
    Long articleId;
    String purchaserNickname;
}
