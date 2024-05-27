package HM.Hanbat_Market.domain.entity;

import HM.Hanbat_Market.repository.article.dto.ImageFileDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.SimpleTimeZone;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String uploadFileName;
    private String storeFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public ImageFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    /**
     * 연관관계 메서드
     */
    private void regisArticle(Article article) {
        this.article = article;
        article.getImageFiles().add(this);
    }

    public void format() {
        this.article = null;
    }

    public void setStoreFileName(String fileName) {
        this.storeFileName = fileName;
    }

    /**
     * 생성 메서드
     */

    public static ImageFile newImageFile() {

        return new ImageFile();
    }

    public static ImageFile createImageFile(Article article, ImageFile imageFile) {
        imageFile.regisArticle(article);
        return imageFile;
    }
}


/*
양쪽 세터에서
this.delivery = delivery;
if(!delivery.getOrder().equals(this)) {
 delivery.setOrder(this);
}
이런식의 패턴을 쓰면 그냥 setter 여는게아니라 괜찮습니다

일단 세팅 편의 메서드를 구상하시는데 결국 이게 세터나 다름 없어서 고민하시는 것 같아 무조건 쓰지마라가 경우에 따라 이유있는 세팅 메서드라면 사용하셔도 된다는 뜻으로 말씀드렸네유
 */