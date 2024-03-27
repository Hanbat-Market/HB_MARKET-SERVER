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
public class ArticleCreateRequestDto {

    @NotEmpty(message = "제목을 입력해주세요..")
    private String title;

    @NotNull(message = "가격을 입력해주세요..")
    @Min(value = 1, message = "가격은 1 이상이어야 합니다.")
    private Long price;

    @NotEmpty(message = "상품이름을 입력해주세요..")
    private String itemName;

    private String description;

    @NotEmpty(message = "거래장소를 입력해주세요..")
    private String tradingPlace;

    @Hidden
    private List<MultipartFile> imageFiles;
}