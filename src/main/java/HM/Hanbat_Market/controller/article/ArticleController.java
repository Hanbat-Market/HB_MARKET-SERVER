package HM.Hanbat_Market.controller.article;

import HM.Hanbat_Market.controller.member.dto.MemberForm;
import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.item.ImageFileRepository;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;
    private final PreemptionItemService preemptionItemService;
    private final FileStore fileStore = new FileStore();

    @GetMapping("/articles/new")
    public String createForm(@ModelAttribute("articleForm") ArticleForm articleForm,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember, Model model) {

        if (sessionMember == null) {
            return "redirect:/";
        }

        model.addAttribute("member", sessionMember);
        List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(sessionMember);
        model.addAttribute("memberPreemptionSize", preemptionItemByMember.size());
        return "article/registerArticle";
    }

    @PostMapping("/articles/new")
    public String create(@Valid @ModelAttribute ArticleForm form, BindingResult result,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember,
                         Model model) throws IOException {
        if (sessionMember == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.info(String.valueOf(result.getFieldError("imageFiles1")));
            model.addAttribute("member", sessionMember);
            return "article/registerArticle";
        }

        ArticleCreateDto articleCreateDto = new ArticleCreateDto();
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setItemName(form.getItemName());
        itemCreateDto.setPrice(form.getPrice());
        articleCreateDto.setTitle(form.getTitle());
        articleCreateDto.setDescription(form.getDescription());
        articleCreateDto.setTradingPlace(form.getTradingPlace());

        List<MultipartFile> multipartFiles = new ArrayList<>();
        if (form.getImageFile1() != null) {
            multipartFiles.add(form.getImageFile1());
        }

        // 파일 유효성 검사 실패 시 에러 추가
        if (!multipartFiles.isEmpty()) {
            List<ImageFile> imageFiles = fileStore.storeFiles(multipartFiles, result);
            articleCreateDto.setImageFiles(imageFiles);
        }

        if (result.getFieldError("imageFile1") != null) {
            log.info(String.valueOf(result.getFieldError("imageFiles1")));
            model.addAttribute("member", sessionMember);
            return "article/registerArticle";
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
        model.addAttribute("member", sessionMember);
        model.addAttribute("articles", articles);
        List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(sessionMember);
        model.addAttribute("memberPreemptionSize", preemptionItemByMember.size());
        return "/";
    }

    @GetMapping("/articles/{articleId}")
    public String articleDetail(@PathVariable("articleId") Long articleId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member sessionMember, Model model) {

        if (sessionMember == null) {
            return "redirect:/";
        }

        Article article = articleService.findArticle(articleId);
        log.info("@@@___@@@@@@@@@@@@");
        log.info(String.valueOf(article.getImageFiles().size()));
        log.info("@@@___@@@@@@@@@@@@");
        model.addAttribute("member", sessionMember);
        model.addAttribute("article", article);
        List<PreemptionItem> preemptionItemByMember = preemptionItemService.findPreemptionItemByMember(sessionMember);
        model.addAttribute("memberPreemptionSize", preemptionItemByMember.size());
        return "article/detailArticle";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable("filename") String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
