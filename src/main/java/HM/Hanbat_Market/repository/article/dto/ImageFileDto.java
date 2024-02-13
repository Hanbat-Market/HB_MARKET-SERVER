package HM.Hanbat_Market.repository.article.dto;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.ImageFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ImageFileDto {
    String uploadFileName;
    String storeFileName;
    private List<ImageFile> imageFiles;
}
