package HM.Hanbat_Market.api.article.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ArticleUpdateRequestDto {

    private String title;

    private Long price;

    private String itemName;

    private String description;

    private String tradingPlace;

    @Hidden
    private List<MultipartFile> imageFiles;
}
