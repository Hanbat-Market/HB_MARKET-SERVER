package HM.Hanbat_Market.repository.article;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.ArticleStatus;
import HM.Hanbat_Market.domain.entity.ItemStatus;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaArticleRepository implements ArticleRepository {

    private final EntityManager em;

    @Override
    public Article save(Article article) {
        em.persist(article);
        return article;
    }

    @Override
    public Optional<Article> findById(Long id) {
        Article article = em.find(Article.class, id);
        return Optional.ofNullable(article);
    }

    @Override
    public List<Article> findAll() {
        return em.createQuery("select a from Article a join a.item i where" +
                        " a.articleStatus != :articleStatusHide and i.itemStatus != :itemStatusHide", Article.class)
                .setParameter("articleStatusHide", ArticleStatus.HIDE)
                .setParameter("itemStatusHide", ItemStatus.HIDE)
                .getResultList();
    }

    @Override
    public List<Article> findAllByMember(Member member) {
        Long memberId = member.getId();
        return em.createQuery("select a from Article a join a.member m join a.item i where m.id = :memberId" +
                        " and a.articleStatus != :articleStatusHide and i.itemStatus != :itemStatusHide", Article.class)
                .setParameter("memberId", member.getId())
                .setParameter("articleStatusHide", ArticleStatus.HIDE)
                .setParameter("itemStatusHide", ItemStatus.HIDE)
                .getResultList();
    }

    @Override
    public List<Article> findAllBySearch(ArticleSearchDto articleSearchDto) {
        //language=JPAQL
        String jpql = "select a From Article a join fetch a.item i join fetch a.member m";
        boolean isFirstCondition = true;
        //아이템 상태 검색
        if (articleSearchDto.getItemStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " i.itemStatus = :itemStatus";
        }
        //게시글 제목 검색
        if (StringUtils.hasText(articleSearchDto.getTitle())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += "  a.title like concat('%', :title, '%')";
        }
        //아이템 이름 검색
        if (StringUtils.hasText(articleSearchDto.getItemName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " i.itemName like concat('%', :itemName, '%')";
        }
        //삭제된 게시글 제외
        if (isFirstCondition) {
            jpql += " where";
            isFirstCondition = false;
        } else {
            jpql += " and";
        }
        jpql += " a.articleStatus != :articleStatusHide and i.itemStatus != :itemStatusHide";

        TypedQuery<Article> query = em.createQuery(jpql, Article.class)
                .setMaxResults(1000); //최대 1000건
        if (articleSearchDto.getItemStatus() != null) {
            query = query
                    .setParameter("itemStatus", articleSearchDto.getItemStatus());
        }
        if (StringUtils.hasText(articleSearchDto.getItemName())) {
            query = query
                    .setParameter("itemName", articleSearchDto.getItemName());
        }
        if (StringUtils.hasText(articleSearchDto.getTitle())) {
            query = query
                    .setParameter("title", articleSearchDto.getTitle());
        }
        query = query
                .setParameter("articleStatusHide", ArticleStatus.HIDE)
                .setParameter("itemStatusHide", ItemStatus.HIDE);
        return query.getResultList();
    }
}
