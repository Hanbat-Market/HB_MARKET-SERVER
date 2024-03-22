package HM.Hanbat_Market.api.dto;

import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
public class HomeResponseDto {
    int articlesCount;
    List<HomeArticlesDto> articles;
}
