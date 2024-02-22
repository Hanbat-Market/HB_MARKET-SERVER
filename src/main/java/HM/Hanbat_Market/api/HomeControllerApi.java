package HM.Hanbat_Market.api;

import HM.Hanbat_Market.api.dto.HomeArticlesDto;
import HM.Hanbat_Market.api.dto.HomeResponseDto;
import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class HomeControllerApi {

    private final ArticleService articleService;
    private final PreemptionItemService preemptionItemService;
    private final MemberService memberService;
    private final ItemRepository itemRepository;

    @GetMapping("")
    public HomeResponseDto home(@RequestBody ArticleSearchDto articleSearchDto) {
        /**
         * 토큰으로 멤버 인증하고 넣어주는 과정 필요함
         */
        Member loginMember = memberService.findOne("jckim2").get(); //시큐리티 구현전 임시 더미 데이터 멤버

        List<Article> articles = articleService.findArticles(articleSearchDto);
        List<HomeArticlesDto> homeArticlesDtos = articleService.findArticlesToDto(articles);
        List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);

        return new HomeResponseDto(preemptionItemByMember.size(), homeArticlesDtos.size(), homeArticlesDtos);
    }
}
