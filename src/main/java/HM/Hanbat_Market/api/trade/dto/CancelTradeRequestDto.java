package HM.Hanbat_Market.api.trade.dto;

import HM.Hanbat_Market.domain.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CancelTradeRequestDto {

    private Long tradeId;

    private String requestMemberNickname;
}
