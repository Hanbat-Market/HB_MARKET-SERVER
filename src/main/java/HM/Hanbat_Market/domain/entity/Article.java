package HM.Hanbat_Market.domain.entity;

import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ArticleUpdateDto;
import HM.Hanbat_Market.repository.item.dto.ItemUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.awt.*;
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
    private Long id;

    private String title;

    private String description;

    private String tradingPlace;

    private ArticleStatus articleStatus;

    @OneToOne(mappedBy = "article", cascade = CascadeType.ALL)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ImageFile> imageFiles = new ArrayList<>();

    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    private Article(String title, String description, String tradingPlace) {
        this.title = title;
        this.description = description;
        this.tradingPlace = tradingPlace;
        this.createdAt = LocalDateTime.now();
        this.articleStatus = ArticleStatus.OPEN;
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
     * 생성 메서드
     */
    public static Article create(ArticleCreateDto articleCreateDto) {
        Article article = new Article(articleCreateDto.getTitle(),
                articleCreateDto.getDescription(), articleCreateDto.getTradingPlace());

        article.regisMember(articleCreateDto.getMember());
        article.regisItem(articleCreateDto.getItem());

        return article;
    }

    public void update(ArticleUpdateDto articleUpdateDto) {
        this.title = articleUpdateDto.getTitle();
        this.description = articleUpdateDto.getDescription();
        this.tradingPlace = articleUpdateDto.getDescription();
        this.item.updateItem(articleUpdateDto.getItemUpdateDto());
    }

    public void delete() {
        this.articleStatus = ArticleStatus.HIDE;
        this.item.deleteItem();
    }
}
