package HM.Hanbat_Market.api.article.dto;

import HM.Hanbat_Market.domain.entity.ImageFile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImageFileDto {
    String uploadFileName;
    String storeFileName;
    private List<ImageFile> imageFiles;
}
