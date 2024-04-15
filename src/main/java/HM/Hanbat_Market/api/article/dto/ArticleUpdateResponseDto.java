package HM.Hanbat_Market.api.article.dto;

import HM.Hanbat_Market.domain.entity.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArticleUpdateResponseDto {

    private String uuid;

    private String title;

    private String description;

    private String tradingPlace;

    private String itemName;

    private Long price;

    private List<String> filePaths;
}
