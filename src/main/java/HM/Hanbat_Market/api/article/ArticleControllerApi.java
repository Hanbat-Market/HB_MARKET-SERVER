package HM.Hanbat_Market.api.article;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.article.dto.ArticleCreateRequestDto;
import HM.Hanbat_Market.api.article.dto.ArticleCreateResponseDto;
import HM.Hanbat_Market.api.article.dto.ArticleUpdateRequestDto;
import HM.Hanbat_Market.api.article.dto.ArticleUpdateResponseDto;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ArticleControllerApi {

    private final ArticleService articleService;
    private final FileStore fileStore = new FileStore();

    @PostMapping("/articles/new")
    public Result create(@RequestPart("imageFile") MultipartFile imageFile,
                         @Valid @RequestPart("articleCreateRequestDto") ArticleCreateRequestDto articleCreateRequestDto,
                         BindingResult result,
                         @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                                 required = false) Member sessionMember) throws IOException {

        articleCreateRequestDto.setImageFile1(imageFile);
        ArticleCreateResponseDto articleCreateResponseDto = articleService.createArticleToDto(sessionMember.getId(), articleCreateRequestDto);

        return new Result(articleCreateResponseDto);
    }

    @GetMapping("/articles/edit/{articleId}")
    public Result updateDetail(@PathVariable("articleId") Long articleId,
                               @Parameter(hidden = true)
                               @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                               Member sessionMember) {

        Article article = articleService.findArticle(articleId);

        return new Result(articleService.articleDetailToDto(article, sessionMember));
    }

    @PatchMapping("/articles/edit/{articleId}")
    public Result update(@PathVariable("articleId") Long articleId,
                         @RequestPart("imageFile") MultipartFile imageFile,
                         @RequestPart("articleUpdateRequestDto") ArticleUpdateRequestDto articleUpdateRequestDto,
                         @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                                 required = false) Member sessionMember) throws IOException {

        articleUpdateRequestDto.setImageFile1(imageFile);
        ArticleUpdateResponseDto articleUpdateResponseDto = articleService.updateArticleToDto(articleId, articleUpdateRequestDto, sessionMember);

        return new Result(articleUpdateResponseDto);
    }

    @PostMapping("/articles/delete/{articleId}")
    public Result delete(@PathVariable("articleId") Long articleId,
                         @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                                 required = false) Member sessionMember) throws IOException {

        articleService.deleteArticle(articleId, sessionMember);

        return new Result("ok");
    }

    @GetMapping("/articles/{articleId}")
    public Result articleDetail(@PathVariable("articleId") Long articleId,
                                @Parameter(hidden = true)
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                                Member sessionMember) {

        Article article = articleService.findUpdateArticle(articleId, sessionMember);
        return new Result(articleService.articleDetailToDto(article, sessionMember));
    }

    @ResponseBody
    @Hidden
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable("filename") String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}