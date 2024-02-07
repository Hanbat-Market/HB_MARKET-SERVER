package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Trade {

    @Id
    @GeneratedValue
    @Column(name = "trade_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime tradeDate;

    private TradeStatus tradeStatus;


    /**
     * 연관관계 편의 메서드
     */
    private void regisMember(Member member) {
        this.member = member;
        member.getTrades().add(this);
    }

    private void regisItem(Item item) {
        this.item = item;
        item.setTrade(this);
    }

    public static Trade reservation(Member member, Item item) {
        Trade trade = new Trade();
        trade.regisItem(item);
        trade.regisMember(member);
        trade.tradeStatus = TradeStatus.RESERVATION;
        return trade;
    }

    public Trade complete() {
        this.tradeStatus = TradeStatus.COMP;
        return this;
    }

    public void cancel(){
        this.tradeStatus = TradeStatus.CANCEL;
    }
}
