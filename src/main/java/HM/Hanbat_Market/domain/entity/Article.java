package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private AtomicLong id = new AtomicLong();
    ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ImageFile> imageFiles = new ArrayList<>();

    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    private Article(Member member, Item item) {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 연관관계 편의 메서드
     */
    private void regisMember(Member member) {
        this.member = member;
        member.getArticles().add(this);
    }

    private void regisItem(Item item) {
        this.item = item;
        item.setArticle(this);
    }

    /**
     * @param member
     * @param item   생성 메서드
     */
    public static Article createArticle(Member member, Item item) {
        Article article = new Article(member, item);
        article.regisMember(member);
        article.regisItem(item);
        return article;
    }

}
