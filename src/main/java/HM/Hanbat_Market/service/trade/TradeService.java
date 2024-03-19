package HM.Hanbat_Market.service.trade;

import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.Trade;
import HM.Hanbat_Market.domain.entity.TradeStatus;
import HM.Hanbat_Market.exception.trade.AlreadyCompleteTradeException;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ItemRepository itemRepository;

    //판매자의 구매자가 약속을 체결하면 예약
    @Transactional
    public Long reservation(Long memberId, Long itemId) {
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findById(itemId).get();
        Trade trade = Trade.reservation(member, item);

        tradeRepository.save(trade);

        return trade.getId();
    }

    //판매자가 판매를 완료하거나 구매자를 확실히 결정하면 구매완료
    @Transactional
    public Long tradeComplete(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).get();
        trade.complete();
        return tradeId;
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
        trade.cancel();
    }
}
