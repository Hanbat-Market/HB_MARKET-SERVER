package HM.Hanbat_Market.repository.article;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.ItemStatus;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.item.JpaItemRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
        Member member = createTestMember();
        Item item = createTestItem(member, "PS5");
        Article article = creteTestArticle(member, item);

        //when
        jpaArticleRepository.save(article);

        //then
        assertEquals(article, jpaArticleRepository.findById(article.getId()).get());
    }

    @Test
    public void 게시글_아이템_멤버_연관성테스트() throws Exception {
        //given
        Member member = createTestMember();
        jpaMemberRepository.save(member);

        Item item = createTestItem(member, "PS5");
        jpaItemRepository.save(item);

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
        Member member = createTestMember();
        jpaMemberRepository.save(member);

        Member member2 = createTestMember2();
        jpaMemberRepository.save(member2);

        Item item = createTestItem(member, "PS3");
        Item item2 = createTestItem(member, "PS4");
        Item item3 = createTestItem(member2, "PS5");
        jpaItemRepository.save(item);
        jpaItemRepository.save(item2);
        jpaItemRepository.save(item3);

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
        Member member = createTestMember();
        jpaMemberRepository.save(member);

        Member member2 = createTestMember2();
        jpaMemberRepository.save(member2);

        Item item = createTestItem(member, "PS3");
        Item item2 = createTestItem(member, "PS4");
        Item item3 = createTestItem(member2, "PS5");
        jpaItemRepository.save(item);
        jpaItemRepository.save(item2);
        jpaItemRepository.save(item3);

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

    public Member createTestMember() {
        String mail = "jckim229@gmail.com";
        String passwd = "1234";
        String phoneNumber = "010-1234-1234";
        String nickname = "김주찬";

        return Member.createMember(mail, passwd, phoneNumber, nickname);
    }

    public Member createTestMember2() {
        String mail = "wncks0303@naver.com";
        String passwd = "1234";
        String phoneNumber = "010-4321-4321";
        String nickname = "토마스";

        return Member.createMember(mail, passwd, phoneNumber, nickname);
    }

    public Item createTestItem(Member member, String name) {
        String itemName = name;
        Long price = 10000L;
        String description = "hello";
        String tradingPlace = "성수역 7번 출구";

        return Item.createItem(itemName, price, member);
    }


    public Article creteTestArticle(Member member, Item item) {
        String title = "증고책 팝니다";
        String description = "hello";
        String tradingPlace = "성수역 7번 출구";

        return Article.createArticle(title, description, tradingPlace, member, item);
    }

}