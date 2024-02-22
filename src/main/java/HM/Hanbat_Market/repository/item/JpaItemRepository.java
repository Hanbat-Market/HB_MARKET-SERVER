package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaItemRepository implements ItemRepository {

    private final EntityManager em;

    @Transactional
    @Override
    public Item save(Item item) {
        //        if (item.getId() == null){
        //            em.persist(item);
        //        }else {
        //            em.merge(item);
        //        }
        em.persist(item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public Item findAllByArticle(Article article) {
        Long articleId = article.getId();
        return em.createQuery("select i from Item i join i.article a where a.id = :articleId", Item.class)
                .setParameter("articleId", articleId)
                .getSingleResult();
    }

    @Override
    public List<Item> findAllByMember(Member member) {
        Long memberId = member.getId();
        return em.createQuery("select i from Item i join i.member m where m.id = :memberId")
                .setParameter("memberId", member.getId())
                .getResultList();
    }

    @Override
    public List<Item> findAll() {
        return em.createQuery("select i from Item i")
                .getResultList();
    }
}
