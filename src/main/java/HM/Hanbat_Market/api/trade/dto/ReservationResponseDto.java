package HM.Hanbat_Market.api.trade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ReservationResponseDto {

    private final Long tradeId;
    private final String purchaser;
}
