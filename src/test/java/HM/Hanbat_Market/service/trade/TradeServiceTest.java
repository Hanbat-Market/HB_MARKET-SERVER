package HM.Hanbat_Market.service.trade;

import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.trade.TradeRepository;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class TradeServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    ArticleService articleService;
    @Autowired
    TradeService tradeService;
    @Autowired
    TradeRepository tradeRepository;


    @Test
    public void 구매_예약() throws Exception {
        //given
        Member testMember = createTestMember1();
        Member testMember2 = createTestMember2();
        memberService.join(testMember);
        memberService.join(testMember2);

        Long articleId = articleService.regisArticle(testMember.getId(), createArticleCreateDto("test title", "test des", "대전"),
                createItemCreateDto("플스", 170000L));
        //when
        Article article = articleService.findArticle(articleId);
        Trade trade = Trade.reservation(testMember2, article.getItem(), LocalDateTime.now());
        //then
        assertEquals(tradeService.findReservedByMember(testMember2).get(0).getTradeStatus(), TradeStatus.RESERVATION);
    }

    @Test
    public void 구매_완료() throws Exception {
        //given
        Member testMember = createTestMember1();
        Member testMember2 = createTestMember2();
        memberService.join(testMember);
        memberService.join(testMember2);

        Long articleId = articleService.regisArticle(testMember.getId(), createArticleCreateDto("test title", "test des", "대전"),
                createItemCreateDto("플스", 170000L));

        Article article = articleService.findArticle(articleId);
        Long tradeId = tradeService.reservation(testMember2.getId(), article.getItem().getId(), LocalDateTime.now());
        Trade trade = tradeRepository.findById(tradeId).get();
        //when
        tradeService.tradeComplete(testMember2.getNickname(), trade.getItem().getArticle().getId());
        //then
        assertEquals(tradeService.findCompletedByMember(testMember2).get(0).getTradeStatus(), TradeStatus.COMP);
    }

    @Test
    public void 구매_취소() throws Exception {
        //given
        Member testMember = createTestMember1();
        memberService.join(testMember);

        Long articleId = articleService.regisArticle(testMember.getId(), createArticleCreateDto("test title", "test des", "대전"),
                createItemCreateDto("플스", 170000L));

        Article article = articleService.findArticle(articleId);
        Long tradeId = tradeService.reservation(testMember.getId(), article.getItem().getId(), LocalDateTime.now());

        //when
        tradeService.cancelTrade(testMember, testMember.getNickname(), tradeId);

        //then
        assertEquals(tradeService.findReservedByMember(testMember).size(), 0);
    }
}