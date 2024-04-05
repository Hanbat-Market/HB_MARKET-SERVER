package HM.Hanbat_Market.api.trade;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.api.trade.dto.*;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.trade.TradeService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TradeControllerApi {

    private final TradeService tradeService;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @PostMapping("/trade/reservation")
    public Result reservation(@RequestBody ReservationRequestDto reservationRequestDto,
                              HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        //구매자의 멤버 id와 판매 게시글의 id를 통해 예약 체결 (판매자의 권한으로)
        Long reservationId = tradeService.reservation(sessionMember, reservationRequestDto.getPurchaserNickname(), reservationRequestDto.getArticleId(), reservationRequestDto.getTransactionAppointmentDateTime(),
                reservationRequestDto.getReservationPlace());

        return new Result(new ReservationResponseDto(reservationId));
    }

    @PostMapping("/trade/complete")
    public Result complete(@RequestBody CompleteTradeRequestDto completeTradeRequestDto,
                           HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        GetTradeIdResponseDto getTradeIdResponseDto = tradeService.getTradeId(completeTradeRequestDto.getPurchaserNickname(), completeTradeRequestDto.getArticleId());
        tradeService.tradeComplete(sessionMember, getTradeIdResponseDto.getTradeId());

        return new Result("ok");
    }

    @PostMapping("/trade/cancel")
    public Result cancel(@RequestBody CancelTradeRequestDto cancelTradeRequestDto,
                         HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        GetTradeIdResponseDto getTradeIdResponseDto = tradeService.getTradeId(cancelTradeRequestDto.getPurchaserNickname(), cancelTradeRequestDto.getArticleId());
        tradeService.cancelTrade(sessionMember, cancelTradeRequestDto.getRequestMemberNickname(), getTradeIdResponseDto.getTradeId());

        return new Result("ok");
    }
}
