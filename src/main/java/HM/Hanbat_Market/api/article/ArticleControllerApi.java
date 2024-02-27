package HM.Hanbat_Market.api.article;

import HM.Hanbat_Market.api.article.dto.ArticleCreateRequestDto;
import HM.Hanbat_Market.api.article.dto.ArticleCreateResponseDto;
import HM.Hanbat_Market.api.article.dto.ArticleDetailResponseDto;
import HM.Hanbat_Market.controller.article.ArticleForm;
import HM.Hanbat_Market.controller.article.FileStore;
import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ArticleControllerApi {

    private final ArticleService articleService;
    private final PreemptionItemService preemptionItemService;
    private final MemberService memberService;
    private final FileStore fileStore = new FileStore();

    @PostMapping("/articles/new")
    public ArticleCreateResponseDto create(@Valid @RequestBody ArticleCreateRequestDto form, BindingResult result) throws IOException {

        Member member = memberService.findOne("jckim2").get(); //임시 멤버(로그인 멤버 아님 추후 JWT 구현 필요)
        ArticleCreateResponseDto articleCreateResponseDto = articleService.createArticleToDto(member.getId(), form, result);

        return articleCreateResponseDto;
    }

    @GetMapping("/articles/{articleId}")
    public ArticleDetailResponseDto articleDetail(@PathVariable("articleId") Long articleId) {

        Member member = memberService.findOne("jckim2").get(); //임시 멤버(로그인 멤버 아님 추후 JWT 구현 필요)
        Article article = articleService.findArticle(articleId);

        return articleService.articleDetailToDto(article, member);
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable("filename") String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
