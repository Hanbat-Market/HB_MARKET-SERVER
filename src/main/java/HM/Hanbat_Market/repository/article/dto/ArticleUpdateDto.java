package HM.Hanbat_Market.repository.article.dto;

import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.repository.item.dto.ItemUpdateDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArticleUpdateDto {
    private String title;

    private String description;

    private String tradingPlace;

    private ItemUpdateDto itemUpdateDto;

    private List<ImageFile> imageFiles = new ArrayList<>();
}
