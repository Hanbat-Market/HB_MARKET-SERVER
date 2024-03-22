package HM.Hanbat_Market.service.trade;

import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.exception.trade.AlreadyCompleteTradeException;
import HM.Hanbat_Market.exception.trade.IsNotReservationTradeException;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.repository.trade.TradeRepository;
import HM.Hanbat_Market.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public Long reservation(String memberNickname, Long articleId, LocalDateTime transactionAppointmentDateTime) {

        if(transactionAppointmentDateTime == null){
            transactionAppointmentDateTime = LocalDateTime.now();
        }

        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@transactionAppointmentDateTime: " + transactionAppointmentDateTime);
        Article article = articleRepository.findById(articleId).get();
        Member member = memberRepository.findByNickName(memberNickname).get();
        Item item = itemRepository.findById(article.getId()).get();
        Trade trade = Trade.reservation(member, item, transactionAppointmentDateTime);

        tradeRepository.save(trade);

        return trade.getId();
    }

    @Transactional
    public Long reservation(Long memberId, Long articleId, LocalDateTime transactionAppointmentDateTime) {

        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@transactionAppointmentDateTime: " + transactionAppointmentDateTime);

        Article article = articleRepository.findById(articleId).get();
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findById(article.getId()).get();
        Trade trade = Trade.reservation(member, item, transactionAppointmentDateTime);

        tradeRepository.save(trade);

        return trade.getId();
    }

    //판매자가 판매를 완료하거나 구매자를 확실히 결정하면 구매완료
    @Transactional
    public Long tradeComplete(String memberNickname, Long articleId) {

        Article article = articleRepository.findById(articleId).get();
        Member member = memberService.findOne(memberNickname).get();

        Trade reservationByPurchaserAndSeller = tradeRepository.findReservationByPurchaserAndSeller(member, article.getMember(), articleId);

        Trade complete = reservationByPurchaserAndSeller.complete();

        return complete.getId();
    }

    @Transactional
    public Long tradeComplete(Long tradeId) {

        Trade reservationByPurchaserAndSeller = tradeRepository.findById(tradeId).get();

        Trade complete = reservationByPurchaserAndSeller.complete();

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
    public void cancelTrade(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).get();

        if (trade.getTradeStatus() == TradeStatus.COMP) {
            throw new AlreadyCompleteTradeException();
        }
        if(trade.getTradeStatus() == TradeStatus.RESERVATION){
            tradeRepository.remove(trade);
            //        trade.cancel();
        }else{
            throw new IsNotReservationTradeException();
        }
    }
}
