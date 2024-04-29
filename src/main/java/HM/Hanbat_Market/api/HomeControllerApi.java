package HM.Hanbat_Market.api;

import HM.Hanbat_Market.api.dto.HomeArticlesDto;
import HM.Hanbat_Market.api.dto.HomeResponseDto;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.item.ItemRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class HomeControllerApi {

    private final ArticleService articleService;
    private final PreemptionItemService preemptionItemService;
    private final MemberService memberService;
    private final ItemRepository itemRepository;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @GetMapping("")
    public Result home(@ModelAttribute ArticleSearchDto articleSearchDto, HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);
        Member sessionMember = null;
        try {
            sessionMember = memberRepository.findByMail(mail).get();
        } catch (NoSuchElementException e) {
            log.info("첫 로그인");
        }

        List<Article> articles = articleService.findArticles(articleSearchDto);
        List<HomeArticlesDto> homeArticlesDtos = articleService.findArticlesToDto(sessionMember, articles);

        log.info(sessionMember.getMail() + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        return new Result<>(new HomeResponseDto(homeArticlesDtos.size(), homeArticlesDtos));
    }

    /**
     * 인터셉터로 로그인 인가 후 실패시 리다이렉트 될 임시 로그인 페이지
     */
    @Hidden
    @GetMapping("/login")
    public Result login() {
        return new Result("로그인이 필요합니다.₩₩₩");
    }

    @Hidden
    @GetMapping("/login/success")
    public Result loginSuccess() {
        return new Result("OauthLogin success");
    }
}
