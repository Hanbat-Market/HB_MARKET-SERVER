package HM.Hanbat_Market.controller.article;

import HM.Hanbat_Market.controller.member.dto.MemberForm;
import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.service.article.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/articles/new")
    public String createForm(@ModelAttribute("articleForm") ArticleForm articleForm,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember, Model model) {
        model.addAttribute("member", sessionMember);
        if (sessionMember == null) {
            return "redirect:/";
        }
        return "article/registerArticle";
    }

    @PostMapping("/articles/new")
    public String create(@Valid @ModelAttribute ArticleForm form, BindingResult result
            , @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        if (sessionMember == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            return "article/registerArticle";
        }

        ArticleCreateDto articleCreateDto = new ArticleCreateDto();
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setItemName(form.getItemName());
        itemCreateDto.setPrice(form.getPrice());
        articleCreateDto.setTitle(form.getTitle());
        articleCreateDto.setDescription(form.getDescription());
        articleCreateDto.setTradingPlace(form.getTradingPlace());
        if (form.getImageFiles() != null) {
            articleCreateDto.setImageFiles(form.getImageFiles());
        }
        Long articleId = articleService.regisArticle(sessionMember.getId(), articleCreateDto, itemCreateDto);
        return "redirect:/";
    }

    @GetMapping("/articles")
    public String createForm(@ModelAttribute("articleSearch") ArticleSearchDto articleSearchDto,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember,
                             Model model) {
        if (sessionMember == null) {
            return "redirect:/";
        }
        List<Article> articles = articleService.findArticles(articleSearchDto);
        model.addAttribute("articles", articles);
        return "/";
    }
}
