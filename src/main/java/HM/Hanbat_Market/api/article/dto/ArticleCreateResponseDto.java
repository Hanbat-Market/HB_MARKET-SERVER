package HM.Hanbat_Market.api.article.dto;

import HM.Hanbat_Market.domain.entity.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArticleCreateResponseDto {
    private String title;

    private String itemName;

    private List<String> filePaths;
}
