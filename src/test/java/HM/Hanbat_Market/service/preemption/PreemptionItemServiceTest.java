package HM.Hanbat_Market.service.preemption;

import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.item.PreemptionItemRepository;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PreemptionItemServiceTest {
    @Autowired
    PreemptionItemService preemptionItemService;
    @Autowired
    MemberService memberService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    PreemptionItemRepository preemptionItemRepository;

    @Test
    public void 관심_상품_등록() throws Exception {
        //given
        Member testMember = createTestMember1();
        memberService.join(testMember);

        Long articleId = articleService.regisArticle(testMember.getId(), createArticleCreateDto("test title", "test des", "대전"),
                createItemCreateDto("플스", 170000L));
        Article article = articleService.findArticle(articleId);
        Item item = itemRepository.findById(article.getItem().getId()).get();
        //when
        Long preemptionItemId = preemptionItemService.regisPreemption(testMember.getId(), item.getId());
        //then
        PreemptionItem preemptionItem = preemptionItemRepository.findById(preemptionItemId).get();

        assertEquals(preemptionItem, preemptionItemService.findPreemptionItemByMember(testMember).get(0));
        assertEquals(preemptionItem, preemptionItemService.findPreemptionItemByItem(item).get(0));
        assertEquals(PreemptionItemStatus.PREEMPTION, preemptionItem.getPreemptionItemStatus());
    }

    @Test
    public void 관심_상품_취소() throws Exception {
        //given
        Member testMember = createTestMember1();
        memberService.join(testMember);

        Long articleId = articleService.regisArticle(testMember.getId(), createArticleCreateDto("test title", "test des", "대전"),
                createItemCreateDto("플스", 170000L));
        Article article = articleService.findArticle(articleId);
        Item item = itemRepository.findById(article.getItem().getId()).get();
        Long preemptionItemId = preemptionItemService.regisPreemption(testMember.getId(), item.getId());
        PreemptionItem preemptionItem = preemptionItemRepository.findById(preemptionItemId).get();
        //when
        preemptionItemService.cancelPreemption(preemptionItemId);
        //then
        assertEquals(0, preemptionItemService.findAllPreemptionItem().size());
        assertEquals(PreemptionItemStatus.CANCEL, preemptionItem.getPreemptionItemStatus());
    }
}