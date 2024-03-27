package HM.Hanbat_Market.api.article.dto;

import HM.Hanbat_Market.domain.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class ArticleDetailResponseDto {

    private String title;

    private String description;

    private String tradingPlace;

    private String itemName;

    private Long price;

    private String nickname;

    private List<String> filePaths;

    private LocalDateTime createdAt;

    private PreemptionItemStatus preemptionItemStatus;

    private int preemptionItemSize;

    private ItemStatus itemStatus;
}
