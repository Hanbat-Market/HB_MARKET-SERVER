package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class JpaPreemptionItemRepositoryTest {
    @Autowired
    ArticleRepository jpaArticleRepository;
    @Autowired
    PreemptionItemRepository jpaPreemptionItemRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    ItemRepository jpaItemRepository;

    @Test
    public void 찜_등록() throws Exception {
        //given
        Member newMember = createTestMember1();
        memberService.join(newMember);

        Item testItem = createTestItem(newMember);
        Article article = creteTestArticle(newMember, testItem);

        jpaArticleRepository.save(article);
        //when
        PreemptionItem preemptionItem = PreemptionItem.createPreemptionItem(newMember, testItem);
        jpaPreemptionItemRepository.save(preemptionItem);

        //then
        Item item = jpaItemRepository.findById(testItem.getId()).get();
        assertEquals(preemptionItem, item.getPreemptionItems().get(0));
        assertEquals(preemptionItem, jpaPreemptionItemRepository.findById(preemptionItem.getId()).get());
    }

}