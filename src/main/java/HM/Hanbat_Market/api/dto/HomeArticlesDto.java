package HM.Hanbat_Market.api.dto;

import HM.Hanbat_Market.domain.entity.ArticleStatus;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class HomeArticlesDto {
    private Long id;

    private String title;

    private String description;

    private String tradingPlace;

    private ArticleStatus articleStatus;

    private String itemName;

    private Long price;

    private String memberNickname;

    private String fileName;

    private LocalDateTime createdAt;
}
