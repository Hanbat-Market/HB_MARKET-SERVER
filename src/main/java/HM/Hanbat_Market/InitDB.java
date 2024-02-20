package HM.Hanbat_Market;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ImageFileDto;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import HM.Hanbat_Market.service.trade.TradeService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final ArticleService articleService;
        private final TradeService tradeService;

        public void dbInit1() {
            Member member = Member.createMember("jckim229@gmail.com", "123", "01028564221", "jckim2");
            Member member1 = Member.createMember("wncks0303@naver.com", "123", "01086544221", "wncks0303");
            memberService.join(member);
            memberService.join(member1);

            ItemCreateDto itemCreateDto = new ItemCreateDto();
            itemCreateDto.setItemName("PlayStation4");
            itemCreateDto.setPrice(170000L);

            ArticleCreateDto articleCreateDto = new ArticleCreateDto();
            articleCreateDto.setTitle("플스4 팝니다.");
            articleCreateDto.setDescription("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세");
            articleCreateDto.setTradingPlace("대전 서구 월평동");

            Long articleId = articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto);


            itemCreateDto.setItemName("Nintendo Switch");
            itemCreateDto.setPrice(100000L);

            articleCreateDto.setTitle("닌텐도 스위치 팝니다.");
            articleCreateDto.setDescription("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세");
            articleCreateDto.setTradingPlace("대전 서구 월평동");

            Long articleId1 = articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto);


            itemCreateDto.setItemName("야구방망이");
            itemCreateDto.setPrice(50000L);

            articleCreateDto.setTitle("야구방망이 팝니다.");
            articleCreateDto.setDescription("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세");
            articleCreateDto.setTradingPlace("대전 서구 월평동");

            Long articleId2 = articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto);


            itemCreateDto.setItemName("행정학입문");
            itemCreateDto.setPrice(34000L);

            articleCreateDto.setTitle("행정학 전공서적 팝니다.");
            articleCreateDto.setDescription("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세");
            articleCreateDto.setTradingPlace("대전 서구 월평동");

            Long articleId3 = articleService.regisArticle(member1.getId(), articleCreateDto, itemCreateDto);


            itemCreateDto.setItemName("컴퓨터 개론");
            itemCreateDto.setPrice(25000L);

            articleCreateDto.setTitle("컴퓨터 개론 팝니다.");
            articleCreateDto.setDescription("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세");
            articleCreateDto.setTradingPlace("대전 서구 월평동");

            Long articleId4 = articleService.regisArticle(member1.getId(), articleCreateDto, itemCreateDto);

            Article article = articleService.findArticle(articleId);
            Article article1 = articleService.findArticle(articleId1);
            Article article2 = articleService.findArticle(articleId2);
            Article article3 = articleService.findArticle(articleId3);
            Article article4 = articleService.findArticle(articleId4);

            Long tradeId = tradeService.reservation(member1.getId(), article.getItem().getId());
            Long tradeId1 = tradeService.reservation(member1.getId(), article1.getItem().getId());
            Long tradeId2 = tradeService.reservation(member1.getId(), article2.getItem().getId());
            tradeService.tradeComplete(tradeId2);

            Long tradeId3 = tradeService.reservation(member.getId(), article3.getItem().getId());
            Long tradeId4 = tradeService.reservation(member.getId(), article4.getItem().getId());
            tradeService.tradeComplete(tradeId4);

        }

    }
}
