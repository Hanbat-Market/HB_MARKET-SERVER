package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;

import java.util.List;
import java.util.Optional;

public interface ImageFileRepository {
    Long save(ImageFile imageFile);

    Optional<ImageFile> findById(Long id);

    List<ImageFile> findAll();

    List<ImageFile> findByArticle(Article article);
}
