package HM.Hanbat_Market.api;

import HM.Hanbat_Market.api.dto.HomeArticlesDto;
import HM.Hanbat_Market.api.dto.HomeResponseDto;
import HM.Hanbat_Market.controller.member.login.SessionConst;
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
    public Result home(@RequestBody ArticleSearchDto articleSearchDto
            , @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        /**
         * 토큰으로 멤버 인증하고 넣어주는 과정 필요함
         */

//        if (sessionMember == null) {
//            return new Result<>("로그인이 필요합니다");
//        }

        List<Article> articles = articleService.findArticles(articleSearchDto);
        List<HomeArticlesDto> homeArticlesDtos = articleService.findArticlesToDto(articles);
        List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(sessionMember);

        return new Result<>(new HomeResponseDto(preemptionItemByMember.size(), homeArticlesDtos.size(), homeArticlesDtos));
    }

    /**
     * 인터셉터로 로그인 인가 후 실패시 리다이렉트 될 임시 로그인 페이지
     */
    @GetMapping("/login")
    public Result login(){
        return new Result("로그인이 필요합니다.₩₩₩");
    }
}
