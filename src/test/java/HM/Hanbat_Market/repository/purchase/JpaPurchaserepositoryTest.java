package HM.Hanbat_Market.repository.purchase;

import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.item.PreemptionItemRepository;
import HM.Hanbat_Market.repository.member.JpaMemberRepository;
import HM.Hanbat_Market.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaPurchaserepositoryTest {
    @Autowired
    ArticleRepository jpaArticleRepository;
    @Autowired
    PurchaseRepository jpaPurchaseRepository;
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
        Purchase purchase = Purchase.createPurchase(newMember, testItem);
        jpaPurchaseRepository.save(purchase);

        //then
        Item findItem = jpaItemRepository.findById(testItem.getId()).get();
        Member findMember = memberService.findOne(newMember.getId()).get();

        assertEquals(purchase, findMember.getPurchases().get(0));
        assertEquals(purchase, findItem.getPurchase());
        assertEquals(purchase, jpaPurchaseRepository.findById(purchase.getId()).get());
    }
}