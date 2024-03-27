package HM.Hanbat_Market.api.trade;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.api.trade.dto.CancelTradeRequestDto;
import HM.Hanbat_Market.api.trade.dto.CompleteTradeRequestDto;
import HM.Hanbat_Market.api.trade.dto.ReservationRequestDto;
import HM.Hanbat_Market.api.trade.dto.ReservationResponseDto;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.service.trade.TradeService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TradeControllerApi {

    private final TradeService tradeService;

    @PostMapping("/trade/reservation")
    public Result reservation(@RequestBody ReservationRequestDto reservationRequestDto,
                              @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {

        //구매자의 멤버 id와 판매 게시글의 id를 통해 예약 체결 (판매자의 권한으로)
        Long reservationId = tradeService.reservation(sessionMember ,reservationRequestDto.getMemberNickname(), reservationRequestDto.getArticleId(), reservationRequestDto.getTransactionAppointmentDateTime());

        return new Result(new ReservationResponseDto(reservationId));
    }

    @PostMapping("/trade/complete")
    public Result complete(@RequestBody CompleteTradeRequestDto completeTradeRequestDto,
                           @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {

        tradeService.tradeComplete(sessionMember, completeTradeRequestDto.getTradeId());

        return new Result("ok");
    }

    @PostMapping("/trade/cancel")
    public Result cancel(@RequestBody CancelTradeRequestDto cancelTradeRequestDto,
                         @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {

        tradeService.cancelTrade(sessionMember,cancelTradeRequestDto.getRequestMemberNickname(), cancelTradeRequestDto.getTradeId());

        return new Result("ok");
    }
}
