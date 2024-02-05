package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Long price;

    private Long preemptionCount;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<PreemptionItem> PreemptionItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Purchase> purchases = new ArrayList<>();

    private Item(String itemName, Long price) {
        this.itemName = itemName;
        this.price = price;
        this.preemptionCount = 0L;
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
    public static Item createItem(String itemName, Long price, Member member) {
        Item item = new Item(itemName, price);
        item.regisMember(member);
        return item;
    }

    /**
     * 비즈니스 로직
     */
    public void changeItemStatus() {
        if (this.itemStatus == ItemStatus.SALE) {
            this.itemStatus = ItemStatus.COMP;
        } else if (this.itemStatus == ItemStatus.COMP) {
            this.itemStatus = ItemStatus.SALE;
        }
    }
}
