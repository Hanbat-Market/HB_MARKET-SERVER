package HM.Hanbat_Market.api.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleCreateResponseDto {
    private String title;

    private String itemName;

    private String fileName;
}
