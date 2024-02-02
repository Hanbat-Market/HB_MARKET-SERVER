package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreemptionItem {

    @Id
    @GeneratedValue
    @Column(name = "preemption_item_id")
    private Long id;

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
        member.getPreemptionItems().add(this);
    }

    void regisItem(Item item) {
        this.item = item;
        item.getPreemptionItems().add(this);
    }

    public static PreemptionItem createPreemptionItem(Member member, Item item) {
        PreemptionItem preemptionItem = new PreemptionItem();
        preemptionItem.regisItem(item);
        preemptionItem.regisMember(member);
        return preemptionItem;
    }
}
