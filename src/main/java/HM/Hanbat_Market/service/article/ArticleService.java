package HM.Hanbat_Market.service.article;

import HM.Hanbat_Market.api.article.dto.ArticleCreateRequestDto;
import HM.Hanbat_Market.api.article.dto.ArticleCreateResponseDto;
import HM.Hanbat_Market.api.article.dto.ArticleDetailResponseDto;
import HM.Hanbat_Market.api.dto.HomeArticlesDto;
import HM.Hanbat_Market.controller.article.FileStore;
import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.article.dto.ArticleUpdateDto;
import HM.Hanbat_Market.repository.article.dto.ImageFileDto;
import HM.Hanbat_Market.repository.item.ImageFileRepository;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.preemption.PreemptionItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ImageFileRepository imageFileRepository;
    private final PreemptionItemService preemptionItemService;
    private final FileStore fileStore = new FileStore();

    //영속성 전이로 ItemRepository에 따로 persist하지 않아도 됨
    @Transactional
    public Long regisArticle(Long memberId, ArticleCreateDto articleCreateDto, ItemCreateDto itemCreateDto) {
        Member newMember = memberRepository.findById(memberId).get();
        Item newItem = Item.createItem(itemCreateDto, newMember);
        articleCreateDto.setItem(newItem);
        articleCreateDto.setMember(newMember);
        Article article = Article.create(articleCreateDto);
        articleCreateDto.getImageFiles().stream()
                .forEach(imageFile -> ImageFile
                        .createImageFile(article, imageFile));
        return articleRepository.save(article).getId();
    }

    @Transactional
    public ArticleCreateResponseDto createArticleToDto(Long memberId, ArticleCreateRequestDto form, BindingResult result) throws IOException {

        Member newMember = memberRepository.findById(memberId).get();

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

        Item newItem = Item.createItem(itemCreateDto, newMember);
        articleCreateDto.setItem(newItem);
        articleCreateDto.setMember(newMember);
        Article article = Article.create(articleCreateDto);

        if (!articleCreateDto.getImageFiles().isEmpty()) {
            articleCreateDto.getImageFiles().stream()
                    .forEach(imageFile -> ImageFile
                            .createImageFile(article, imageFile));
        } else if (articleCreateDto.getImageFiles().isEmpty()) {
            ImageFile imageFile = new ImageFile("default_image", "default_image");
            ImageFile.createImageFile(article, imageFile);
        }

        articleRepository.save(article).getId();
        return new ArticleCreateResponseDto(
                article.getTitle(),
                article.getItem().
                        getItemName(),
                article.getImageFiles()
                        .get(0)
                        .getStoreFileName());
    }

    @Transactional
    public ArticleDetailResponseDto articleDetailToDto(Article article, Member loginMember) {
        Article findArticle = articleRepository.findById(article.getId()).get();
        Item item = findArticle.getItem();
        Member member = findArticle.getMember();
        String fileName = null;
        try {
            fileName = findArticle.getImageFiles().get(0).getStoreFileName();
        } catch (IndexOutOfBoundsException ex) {
            log.info("이미지 파일이 null 입니다.");
        }
        int preemptionItemSize = preemptionItemService.findPreemptionItemByMember(loginMember).size();


        return new ArticleDetailResponseDto(
                findArticle.getTitle(),
                findArticle.getDescription(),
                findArticle.getTradingPlace(),
                findArticle.getArticleStatus(),
                item.getItemName(),
                item.getPrice(),
                member.getNickname(),
                fileName,
                findArticle.getCreatedAt(),
                preemptionItemSize);
    }

    //검색
    public List<Article> findArticles(ArticleSearchDto articleSearchDto) {
        return articleRepository.findAllBySearch(articleSearchDto);
    }

    public List<HomeArticlesDto> findArticlesToDto(List<Article> articles) {
        List<HomeArticlesDto> homeArticlesDtos = articles.stream()
                .map(a -> {
                    List<ImageFile> imageFiles = imageFileRepository.findByArticle(a);
                    String storeFileName = null;
                    if (imageFiles != null && !imageFiles.isEmpty()) {
                        storeFileName = imageFiles.get(0).getStoreFileName();
                    }

                    return new HomeArticlesDto(
                            a.getId(),
                            a.getTitle(),
                            a.getDescription(),
                            a.getTradingPlace(),
                            a.getArticleStatus(),
                            a.getItem().getItemName(),
                            a.getItem().getPrice(),
                            a.getMember().getNickname(),
                            storeFileName,
                            a.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
        return homeArticlesDtos;
    }


    public List<Article> findArticles() {
        return articleRepository.findAll();
    }

    public List<Article> findArticles(Member member) {
        return articleRepository.findAllByMember(member);
    }

    public Article findArticle(Long articleId) {
        return articleRepository.findById(articleId).get();
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).get();
        article.delete();
    }

    @Transactional
    public Long updateArticle(Long articleId, ArticleUpdateDto articleUpdateDto) {
        Article article = articleRepository.findById(articleId).get();
        article.update(articleUpdateDto);
        return articleId;
    }


}
