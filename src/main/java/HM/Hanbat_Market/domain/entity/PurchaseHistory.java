package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseHistory {

    @Id
    @GeneratedValue
    @Column(name = "purchase_history_id")
    private Long id;
    ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * 연관관계 편의 메서드
     */
    private void regisMember(Member member) {
        this.member = member;
        member.getPurchaseHistories().add(this);
    }

    private void regisItem(Item item) {
        this.item = item;
        item.getPurchaseHistories().add(this);
    }

    public static PurchaseHistory createPurchaseHistory(Member member, Item item) {
        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.regisItem(item);
        purchaseHistory.regisMember(member);
        return purchaseHistory;
    }
}
