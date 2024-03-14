package HM.Hanbat_Market.api.article;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.article.dto.ArticleCreateRequestDto;
import HM.Hanbat_Market.api.article.dto.ArticleCreateResponseDto;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PreemptionItemService preemptionItemService;
    private final MemberService memberService;
    private final FileStore fileStore = new FileStore();

    @PostMapping("/articles/new")
    public Result create(@RequestPart("imageFile") MultipartFile imageFile,
                         @Valid @RequestPart("articleCreateRequestDto") ArticleCreateRequestDto articleCreateRequestDto,
                         BindingResult result,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) throws IOException {

//        if (sessionMember == null) {
//            return new Result<>("로그인이 필요합니다");
//        }

        articleCreateRequestDto.setImageFile1(imageFile);
        ArticleCreateResponseDto articleCreateResponseDto = articleService.createArticleToDto(sessionMember.getId(), articleCreateRequestDto, result);

        return new Result(articleCreateResponseDto);
    }

    @GetMapping("/articles/{articleId}")
    public Result articleDetail(@PathVariable("articleId") Long articleId,
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {

//        if (sessionMember == null) {
//            return new Result<>("로그인이 필요합니다");
//        }

        Article article = articleService.findArticle(articleId);

        return new Result(articleService.articleDetailToDto(article, sessionMember));
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Result downloadImage(@PathVariable("filename") String filename,
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) throws MalformedURLException {
//        if (sessionMember == null) {
//            return new Result<>("로그인이 필요합니다");
//        }
        return new Result(new UrlResource("file:" + fileStore.getFullPath(filename)));
    }
}
