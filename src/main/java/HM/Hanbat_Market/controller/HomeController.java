package HM.Hanbat_Market.controller;

import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.PreemptionItem;
import HM.Hanbat_Market.domain.entity.PreemptionItemStatus;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final ArticleService articleService;
    private final PreemptionItemService preemptionItemService;

    @GetMapping("/")
    public String home(@ModelAttribute("articleSearch") ArticleSearchDto articleSearchDto,
                       @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            log.info("세션 없음");
            return "redirect:/login";
        }
        log.info("세션 확인");
        model.addAttribute("member", loginMember);
        List<Article> articles = articleService.findArticles(articleSearchDto);
//        List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);
//
//        List<Article> modelArticles = new ArrayList<>();
//        for (PreemptionItem preemptionItem : preemptionItemByMember) {
//            modelArticles.add(preemptionItem.getItem().getArticle());
//        }
//        for (Article article : articles) {
//            if (!modelArticles.contains(article)) {
//                modelArticles.add(article);
//            }
//        }
        List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(loginMember);
        model.addAttribute("articles", articles);
        model.addAttribute("memberPreemptionSize", preemptionItemByMember.size());

        return "memberHome";
    }
}
