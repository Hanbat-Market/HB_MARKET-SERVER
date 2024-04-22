package HM.Hanbat_Market.repository.article;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.ItemStatus;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class JpaArticleRepositoryTest {
    @Autowired
    ItemRepository jpaItemRepository;
    @Autowired
    MemberRepository jpaMemberRepository;
    @Autowired
    ArticleRepository jpaArticleRepository;

    @Test
    public void 게시글_등록() throws Exception {
        //given
        Member member = createTestMember1();
        Item item = createTestItem(member);
        Article article = creteTestArticle(member, item);

        //when
        jpaArticleRepository.save(article);

        //then
        assertEquals(article, jpaArticleRepository.findById(article.getId()).get());
    }

    @Test
    public void 게시글_아이템_멤버_연관성테스트() throws Exception {
        //given
        Member member = createTestMember1();
        jpaMemberRepository.save(member);

        Item item = createTestItem(member);
        Article article = creteTestArticle(member, item);

        jpaArticleRepository.save(article);

        //when
        Article findArticle = jpaArticleRepository.findById(article.getId()).get();
        Item findItem = jpaItemRepository.findById(findArticle.getItem().getId()).get();
        Member findMember = jpaMemberRepository.findById(findArticle.getMember().getId()).get();

        //then
        assertEquals(article, jpaArticleRepository.findById(article.getId()).get());
        assertEquals(findItem, jpaItemRepository.findById(item.getId()).get());
        assertEquals(findMember, jpaMemberRepository.findById(member.getId()).get());

        assertEquals(article, findArticle);
        assertEquals(item, findItem);
        assertEquals(member, findMember);
    }

    /**
     * 모든 게시글을 조회하는 것을 테스트 합니다.
     * 멤버와 아이템과 게시글의 연관성을 테스트합니다.
     */

    @Test
    public void 게시글_전체조회() throws Exception {
        //given
        Member member = createTestMember1();
        Member member2 = createTestMember2();

        jpaMemberRepository.save(member);
        jpaMemberRepository.save(member2);

        Item item = createTestItem(member, "PS3", 16000L);
        Item item2 = createTestItem(member, "PS4", 160000L);
        Item item3 = createTestItem(member2, "PS5", 160000L);

        Article article = creteTestArticle(member, item);
        Article article2 = creteTestArticle(member, item2);
        Article article3 = creteTestArticle(member2, item3);

        jpaArticleRepository.save(article);
        jpaArticleRepository.save(article2);
        jpaArticleRepository.save(article3);

        Member findMember = jpaMemberRepository.findById(member.getId()).get();

        //when
        List<Article> articles = jpaArticleRepository.findAll();
        List<Article> memberArticles = jpaArticleRepository.findAllByMember(findMember);

        //then
        assertEquals(3, articles.size());
        assertEquals(2, findMember.getItems().size());
        assertEquals(2, memberArticles.size());
    }

    @Test
    public void 게시글_검색() throws Exception {
        //given
        Member member = createTestMember1();
        Member member2 = createTestMember2();

        jpaMemberRepository.save(member);
        jpaMemberRepository.save(member2);

        Item item = createTestItem(member, "PS3", 16000L);
        Item item2 = createTestItem(member, "PS4", 160000L);
        Item item3 = createTestItem(member2, "PS5", 160000L);

        Article article = creteTestArticle(member, item);
        Article article2 = creteTestArticle(member, item2);
        Article article3 = creteTestArticle(member2, item3);

        jpaArticleRepository.save(article);
        jpaArticleRepository.save(article2);
        jpaArticleRepository.save(article3);

        ArticleSearchDto articleSearchDto = new ArticleSearchDto();

        //when
        List<Article> searchAll = jpaArticleRepository.findAllBySearch(articleSearchDto);

        articleSearchDto.setItemName("PS5");
        List<Article> searchByItemName = jpaArticleRepository.findAllBySearch(articleSearchDto);

        articleSearchDto.setItemName(null);
        articleSearchDto.setItemStatus(ItemStatus.COMP);
        item.changeItemStatus();

        List<Article> searchByItemStatus = jpaArticleRepository.findAllBySearch(articleSearchDto);

        //then
        assertEquals(searchAll.size(), 3);
        assertEquals(searchByItemName.size(), 1);
        assertEquals(searchByItemStatus.size(), 1);
    }
}