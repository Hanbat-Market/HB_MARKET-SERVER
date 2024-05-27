package HM.Hanbat_Market.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime transactionAppointmentDateTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // NULL 허용하지 않음
    private Item item;

    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    private String reservationPlace;


    /**
     * 연관관계 편의 메서드
     */
    private void regisMember(Member member) {
        try {
            Member beforePurchaser = this.member;
            beforePurchaser.getTrades().remove(this);
            this.member = member;
            member.getTrades().add(this);
        } catch (NullPointerException e) {
            this.member = member;
            member.getTrades().add(this);
        }
    }

    private void regisItem(Item item) {
        this.item = item;
        item.setTrade(this);
    }

    /**
     * 비즈니스 로직
     */
    public static Trade reservation(Member member, Item item, LocalDateTime transactionAppointmentDateTime, String reservationPlace) {
        Trade trade = new Trade();
        trade.transactionAppointmentDateTime = setCreatedAt(transactionAppointmentDateTime);
        trade.regisItem(item);
        trade.regisMember(member);
        trade.tradeStatus = TradeStatus.RESERVATION;
        item.reservationItemStatus();
        trade.reservationPlace = reservationPlace;
        return trade;
    }

    public Trade complete() {
        this.tradeStatus = TradeStatus.COMP;
        this.item.compItemStatus();
        return this;
    }

    public void StatusToReservation(Member purchaser) {
        this.transactionAppointmentDateTime = setCreatedAt(transactionAppointmentDateTime);
        this.regisItem(item);
        this.regisMember(purchaser);
        this.tradeStatus = TradeStatus.RESERVATION;
        item.reservationItemStatus();
        this.reservationPlace = reservationPlace;
    }

    public void cancel() {
        this.tradeStatus = TradeStatus.CANCEL;
        this.item.cancelReservation();
    }

    private static LocalDateTime setCreatedAt(LocalDateTime createdAt) {
        // LocalDateTime 값을 밀리초로 변환하여 설정
        return createdAt.withNano(0);
    }
}
