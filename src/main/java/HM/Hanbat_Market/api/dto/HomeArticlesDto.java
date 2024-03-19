package HM.Hanbat_Market.api.dto;

import HM.Hanbat_Market.domain.entity.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
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

    private List<String> filePaths;

    private LocalDateTime createdAt;
}
