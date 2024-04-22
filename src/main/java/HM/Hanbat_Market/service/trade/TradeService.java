package HM.Hanbat_Market.service.trade;

import HM.Hanbat_Market.api.trade.dto.GetTradeIdResponseDto;
import HM.Hanbat_Market.api.trade.dto.ReservationResponseDto;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.exception.NotFoundException;
import HM.Hanbat_Market.exception.trade.*;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.repository.trade.TradeRepository;
import HM.Hanbat_Market.service.member.MemberService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ItemRepository itemRepository;
    private final MemberService memberService;

    //판매자의 구매자가 약속을 체결하면 예약
    @Transactional
    public Long reservation(Member loginMember, String purchaserNickname, Long articleId, LocalDateTime transactionAppointmentDateTime,
                            String reservationPlace) {

        if (transactionAppointmentDateTime == null) {
            transactionAppointmentDateTime = LocalDateTime.now();
        }
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@transactionAppointmentDateTime: " + transactionAppointmentDateTime);
        Trade trade = null;

        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundException::new);

        Member member = memberRepository.findByNickName(purchaserNickname).get();
        Item item = itemRepository.findById(article.getId()).get();

        if (item.getMember().getId() != loginMember.getId()) {
            throw new OnlyReservationOwnerException();
        }

        try {
            trade = tradeRepository.findByItem(item.getId()).get();
            if (trade.getTradeStatus() == TradeStatus.COMP) {
                throw new AlreadyCompleteTradeCantReservationException();
            }
            trade.StatusToReservation(member);
        } catch (NoResultException e) {
            trade = Trade.reservation(member, item, transactionAppointmentDateTime, reservationPlace);
        }

        tradeRepository.save(trade);

        return trade.getId();
    }

    @Transactional
    public Long reservation(Long memberId, Long articleId, LocalDateTime transactionAppointmentDateTime) {

        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@transactionAppointmentDateTime: " + transactionAppointmentDateTime);

        Article article = articleRepository.findById(articleId).get();
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findById(article.getId()).get();

        try {
            if (item.getTrade().getTradeStatus() == TradeStatus.RESERVATION) {
                throw new AlreadyReservationException();
            }
        } catch (NullPointerException e) {
            log.info("첫 예약입니다.");
        }

        Trade trade = Trade.reservation(member, item, transactionAppointmentDateTime, "임시 예약 장소");

        tradeRepository.save(trade);

        return trade.getId();
    }

    public ReservationResponseDto mappingReservationResponseDto(Trade trade){
        Trade reservationTrade = tradeRepository.findById(trade.getId()).get();

        return new ReservationResponseDto(reservationTrade.getId(), reservationTrade.getMember().getNickname());
    }

    //판매자가 판매를 완료하거나 구매자를 확실히 결정하면 구매완료
    @Transactional
    public Long tradeComplete(String memberNickname, Long articleId) {

        Article article = articleRepository.findById(articleId).get();
        Member member = memberService.findOne(memberNickname).get();

        Trade reservationByPurchaserAndSeller = tradeRepository.findReservationByPurchaserAndSeller(member, article.getMember(), articleId);

        if (reservationByPurchaserAndSeller.getTradeStatus() == TradeStatus.CANCEL) {
            throw new IsCancelTradeException();
        }

        Trade complete = reservationByPurchaserAndSeller.complete();

        return complete.getId();
    }

    @Transactional
    public GetTradeIdResponseDto getTradeId(String purchaserNickname, Long articleId) {

        Article article = articleRepository.findById(articleId).get();
        Item item = article.getItem();
        Member purchaser = memberService.findOne(purchaserNickname).get();
        Member seller = item.getMember();

        if (purchaser.getId() == seller.getId()) {
            throw new PurchaserAndSellerIsSameException();
        }

        Trade reservationByPurchaserAndSeller = tradeRepository.findTradeByPurchaserAndSeller(purchaser, seller, item.getArticle().getId());
        Item findItem = reservationByPurchaserAndSeller.getItem();

        return new GetTradeIdResponseDto(
                findItem.getItemName(),
                findItem.getMember().getNickname(),
                findItem.getTrade().getMember().getNickname(),
                findItem.getTrade().getId(),
                findItem.getItemStatus()
        );
    }

    @Transactional
    public Long tradeComplete(Member loginMember, Long tradeId) {

        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(NotFoundException::new);

        if (trade.getItem().getMember().getId() != loginMember.getId()) {
            throw new OnlyCompleteTradeOwnerException();
        }

        Trade complete = trade.complete();

        return complete.getId();
    }

    //구매내역 조회
    public List<Trade> findCompletedByMember(Member member) {
        return tradeRepository.findCompleteByMember(member);
    }

    //예약중인 거래 조회
    public List<Trade> findReservedByMember(Member member) {
        return tradeRepository.findReservationByMember(member);
    }

    //예약취소(판매자가 다룰 수 있음)
    @Transactional
    public void cancelTrade(Member loginMember, String requestMemberNickName, Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(NotFoundException::new);

        if (trade.getTradeStatus() == TradeStatus.COMP) {
            throw new AlreadyCompleteTradeException();
        }

        Member requestMember = memberService.findOne(requestMemberNickName).get();

        if (requestMember.getId() != trade.getMember().getId() && requestMember.getId() != trade.getItem().getMember().getId()) {
            throw new OnlyCancelTraderException();
        }

        if (trade.getTradeStatus() == TradeStatus.RESERVATION) {
            trade.getItem().cancelReservation();
//            tradeRepository.remove(trade);
            trade.cancel();
        } else {
            throw new IsNotReservationTradeException();
        }
    }
}
