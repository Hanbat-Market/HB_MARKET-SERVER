package HM.Hanbat_Market.api.article;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.article.dto.ArticleCreateRequestDto;
import HM.Hanbat_Market.api.article.dto.ArticleCreateResponseDto;
import HM.Hanbat_Market.api.article.dto.ArticleUpdateRequestDto;
import HM.Hanbat_Market.api.article.dto.ArticleUpdateResponseDto;
import HM.Hanbat_Market.api.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.article.ArticleService;
import HM.Hanbat_Market.service.member.MemberService;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ArticleControllerApi {

    private final ArticleService articleService;
    private final FileStore fileStore;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @PostMapping("/articles/new")
    public Result create(@RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                         @RequestPart("articleCreateRequestDto") ArticleCreateRequestDto articleCreateRequestDto,
                         HttpServletRequest request) throws IOException {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        articleCreateRequestDto.setImageFiles(imageFiles);
        ArticleCreateResponseDto articleCreateResponseDto = articleService.createArticleToDto(sessionMember.getId(), articleCreateRequestDto);


        return new Result(articleCreateResponseDto);
    }

    @GetMapping("/articles/edit/{articleId}")
    @Hidden
    public Result updateDetail(@PathVariable("articleId") Long articleId,
                               HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        Article article = articleService.findUpdateArticle(articleId, sessionMember);
        return new Result(articleService.articleDetailToDto(article, sessionMember));
    }

    @PutMapping("/articles/edit/{articleId}")
    public Result update(@PathVariable("articleId") Long articleId,
                         @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                         @RequestPart("articleUpdateRequestDto") ArticleUpdateRequestDto articleUpdateRequestDto,
                         HttpServletRequest request) throws IOException {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        articleUpdateRequestDto.setImageFiles(imageFiles);
        ArticleUpdateResponseDto articleUpdateResponseDto = articleService.updateArticleToDto(articleId, articleUpdateRequestDto, sessionMember);

        return new Result(articleUpdateResponseDto);
    }

    @DeleteMapping("/articles/delete/{articleId}")
    public Result delete(@PathVariable("articleId") Long articleId,
                         HttpServletRequest request) throws IOException {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        articleService.deleteArticle(articleId, sessionMember);

        return new Result("ok");
    }

    @GetMapping("/articles/{articleId}")
    public Result articleDetail(@PathVariable("articleId") Long articleId,
                                HttpServletRequest request) {

        String token = jwtUtil.resolveTokenFromRequest(request);
        String mail = jwtUtil.getUsername(token);

        Member sessionMember = memberRepository.findByMail(mail).get();

        Article article = articleService.findArticle(articleId);

        return new Result(articleService.articleDetailToDto(article, sessionMember));
    }

//    @ResponseBody
//    @Hidden
//    @GetMapping("/images/{filename}")
//    public Resource downloadImage(@PathVariable("filename") String filename) throws MalformedURLException {
//        return new UrlResource("file:" + fileStore.getFullPath(filename));
//    }
}
