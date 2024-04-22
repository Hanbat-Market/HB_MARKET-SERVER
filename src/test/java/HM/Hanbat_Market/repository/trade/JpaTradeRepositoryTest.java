package HM.Hanbat_Market.repository.trade;

import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class JpaTradeRepositoryTest {
    @Autowired
    ArticleRepository jpaArticleRepository;
    @Autowired
    TradeRepository jpaTradeRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    ItemRepository jpaItemRepository;

    @Test
    public void 구매내역_등록() throws Exception {
        //given
        Member newMember = createTestMember1();
        memberService.join(newMember);

        Item testItem = createTestItem(newMember);
        Article article = creteTestArticle(newMember, testItem);

        jpaArticleRepository.save(article);
        //when
        Trade trade = Trade.reservation(newMember, testItem, LocalDateTime.now(), "임시 저장 장소");
        trade.complete();
        jpaTradeRepository.save(trade);

        //then
        Item findItem = jpaItemRepository.findById(testItem.getId()).get();
        Member findMember = memberService.findOne(newMember.getId()).get();

        assertEquals(trade, findMember.getTrades().get(0));
        assertEquals(trade, findItem.getTrade());
        assertEquals(trade, jpaTradeRepository.findById(trade.getId()).get());
    }
}