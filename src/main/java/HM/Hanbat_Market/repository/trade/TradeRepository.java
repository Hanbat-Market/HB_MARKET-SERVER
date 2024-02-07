package HM.Hanbat_Market.repository.trade;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.Trade;

import java.util.List;
import java.util.Optional;

public interface TradeRepository {
    Long save(Trade trade);

    Optional<Trade> findById(Long id);

    List<Trade> findAll();

    List<Trade> findCompleteByMember(Member member);

    List<Trade> findReservationByMember(Member member);
}
