package HM.Hanbat_Market.api.trade.dto;

import HM.Hanbat_Market.domain.entity.Trade;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReservationRequestDto {

    private String purchaserNickname;

    private Long articleId;

    @Schema(example = "2024-03-21T15:30:45", type = "string", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime transactionAppointmentDateTime;

    private String reservationPlace;
}
