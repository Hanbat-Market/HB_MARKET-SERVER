package HM.Hanbat_Market.repository.article.dto;

import HM.Hanbat_Market.domain.entity.ArticleStatus;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArticleCreateDto {
    private String title;

    private String description;

    private String tradingPlace;

    private Item item;

    private Member member;

    private List<ImageFile> imageFiles = new ArrayList<>();
}
