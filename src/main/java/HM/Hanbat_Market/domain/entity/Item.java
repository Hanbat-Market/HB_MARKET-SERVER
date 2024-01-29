package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private AtomicLong id;

    @Column(nullable = false)
    private AtomicLong price = new AtomicLong();

    private String description;

    private String tradingPlace;

    private AtomicLong preemptionCount = new AtomicLong();

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private Article article;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<PreemptionItem> PreemptionItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<PurchaseHistory> purchaseHistories = new ArrayList<>();

    private Item(AtomicLong price, String description, String tradingPlace, Member member) {
        this.price = price;
        this.description = description;
        this.tradingPlace = tradingPlace;
        this.preemptionCount.set(0);
        this.itemStatus = ItemStatus.SALE;
    }

    //일대일 연관관계 메서드를 위해 세터 생성
    protected void setArticle(Article article) {
        this.article = article;
    }

    /**
     * 연관관계 편의 메서드
     */
    private void regisMember(Member member) {
        this.member = member;
        member.getItems().add(this);
    }

    /**
     * 생성 메서드
     */
    public Item createItem(AtomicLong price, String description, String tradingPlace, Member member) {
        Item item = new Item(price, description, tradingPlace, member);
        item.regisMember(member);
        return item;
    }
}
