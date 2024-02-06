package HM.Hanbat_Market.repository.article;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    Article save(Article article);

    Optional<Article> findById(Long id);

    List<Article> findAll();

    List<Article> findAllByMember(Member member);

    List<Article> findAllBySearch(ArticleSearchDto articleSearchDto);
}
