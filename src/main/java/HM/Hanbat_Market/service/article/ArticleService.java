package HM.Hanbat_Market.service.article;

import HM.Hanbat_Market.api.article.FileStore;
import HM.Hanbat_Market.api.article.dto.*;
import HM.Hanbat_Market.api.dto.HomeArticlesDto;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.exception.ForbiddenException;
import HM.Hanbat_Market.exception.NotFoundException;
import HM.Hanbat_Market.exception.article.FileOutOfRangeException;
import HM.Hanbat_Market.exception.article.IsDeleteArticleException;
import HM.Hanbat_Market.exception.article.NoImageException;
import HM.Hanbat_Market.exception.member.UnAuthorizedException;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.item.ImageFileRepository;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.repository.item.dto.ItemUpdateDto;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.APIURL;
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
import java.util.NoSuchElementException;
import java.util.Optional;
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
    private final FileStore fileStore;
    private final String FILE_URL = APIURL.url;
    private final int IMAGE_MAX_RANGE = 5;
    private final int THUMBNAIL_FILE_INDEX = 0;

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
    public ArticleCreateResponseDto createArticleToDto(Long memberId, ArticleCreateRequestDto form) throws IOException {

        Member newMember = memberRepository.findById(memberId).get();

        ArticleCreateDto articleCreateDto = new ArticleCreateDto();
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setItemName(form.getItemName());
        itemCreateDto.setPrice(form.getPrice());
        articleCreateDto.setTitle(form.getTitle());
        articleCreateDto.setDescription(form.getDescription());
        articleCreateDto.setTradingPlace(form.getTradingPlace());

        List<MultipartFile> multipartFiles = form.getImageFiles();

        if (multipartFiles == null) {
            throw new NoImageException();
        }

        // 파일 유효성 검사 실패 시 에러 추가
        if (multipartFiles.size() > IMAGE_MAX_RANGE) {
            throw new FileOutOfRangeException();
        }

        // 파일 유효성 검사 실패 시 에러 추가
        if (!multipartFiles.isEmpty()) {
            List<ImageFile> imageFiles = fileStore.storeFiles(multipartFiles);
            articleCreateDto.setImageFiles(imageFiles);
        }

        Item newItem = Item.createItem(itemCreateDto, newMember);
        articleCreateDto.setItem(newItem);
        articleCreateDto.setMember(newMember);
        Article article = Article.create(articleCreateDto);

        List<ImageFile> articleImagesFiles = articleCreateDto.getImageFiles();

        if (!articleImagesFiles.isEmpty()) {
            articleCreateDto.getImageFiles().stream()
                    .forEach(imageFile -> ImageFile
                            .createImageFile(article, imageFile));
        } else if (articleImagesFiles.isEmpty()) {
            throw new NoImageException();
        }

        List<String> fileNames = article.getImageFiles().stream()
                .map(imageFile -> getFullPath(imageFile.getStoreFileName()))
                .collect(Collectors.toList());

        articleRepository.save(article).getId();

        return new ArticleCreateResponseDto(
                article.getTitle(),
                article.getItem().
                        getItemName(),
                fileNames
        );
    }

    @Transactional
    public ArticleDetailResponseDto articleDetailToDto(Article article, Member loginMember) {
        Article findArticle = articleRepository.findById(article.getId())
                .orElseThrow(NotFoundException::new);

        if (findArticle.getArticleStatus() == ArticleStatus.HIDE) {
            throw new IsDeleteArticleException();
        }

        Item item = findArticle.getItem();
        Member member = findArticle.getMember();

        PreemptionItemStatus preemptionItemStatus = PreemptionItemStatus.CANCEL;

        PreemptionItem preemptionItemByMemberAndItem = preemptionItemService.findPreemptionItemByMemberAndItem(loginMember, item);

        if (preemptionItemByMemberAndItem != null) {
            preemptionItemStatus = preemptionItemByMemberAndItem.getPreemptionItemStatus();
        }

        int preemptionItemSize = preemptionItemService.findPreemptionItemByItem(item).size();

        List<String> fileNames = findArticle.getImageFiles().stream()
                .map(imageFile -> getFullPath(imageFile.getStoreFileName()))
                .collect(Collectors.toList());

        return new ArticleDetailResponseDto(
                member.getUuid(),
                findArticle.getTitle(),
                findArticle.getDescription(),
                findArticle.getTradingPlace(),
                item.getItemName(),
                item.getPrice(),
                member.getNickname(),
                fileNames,
                findArticle.getCreatedAt(),
                preemptionItemStatus,
                preemptionItemSize,
                item.getItemStatus()
        );
    }

    //검색
    public List<Article> findArticles(ArticleSearchDto articleSearchDto) {
        return articleRepository.findAllBySearch(articleSearchDto);
    }

    public List<HomeArticlesDto> findArticlesToDto(Member loginMember, List<Article> articles) {
        List<HomeArticlesDto> homeArticlesDtos = articles.stream()
                .map(a -> {
                    List<ImageFile> imageFiles = imageFileRepository.findByArticle(a);
                    List<String> fullFilePaths = new ArrayList<>();
                    List<PreemptionItem> preemptionItemByItem = preemptionItemService.findPreemptionItemByItem(a.getItem());

                    PreemptionItemStatus preemptionItemStatus = PreemptionItemStatus.CANCEL;

                    PreemptionItem preemptionItemByMemberAndItem = preemptionItemService.findPreemptionItemByMemberAndItem(loginMember, a.getItem());

                    if (preemptionItemByMemberAndItem != null) {
                        preemptionItemStatus = preemptionItemByMemberAndItem.getPreemptionItemStatus();
                    }

                    if (imageFiles != null && !imageFiles.isEmpty()) {
                        fullFilePaths = imageFiles.stream()
                                .map(imageFile -> getFullPath(imageFile.getStoreFileName()))
                                .collect(Collectors.toList());
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
                            fullFilePaths.get(THUMBNAIL_FILE_INDEX),
                            a.getCreatedAt(),
                            preemptionItemByItem.size(),
                            a.getItem().getItemStatus(),
                            preemptionItemStatus
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
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException());
    }

    public Article findUpdateArticle(Long articleId, Member loginMember) {

        Article article = articleRepository.findById(articleId).get();

        if (confirmOwner(article, loginMember)) {
            throw new ForbiddenException();
        }

        return articleRepository.findById(articleId).get();
    }

    @Transactional
    public void deleteArticle(Long articleId, Member loginMember) {

        Article article = articleRepository.findById(articleId).get();

        if (confirmOwner(article, loginMember)) {
            throw new ForbiddenException();
        }

        article.delete();
        article.getItem().deleteItem();
    }

    @Transactional
    public ArticleUpdateResponseDto updateArticleToDto(Long articleId,
                                                       ArticleUpdateRequestDto articleUpdateRequestDto,
                                                       Member loginMember) throws IOException {

        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setItemName(articleUpdateRequestDto.getItemName());
        itemUpdateDto.setPrice(articleUpdateRequestDto.getPrice());
        articleUpdateDto.setTitle(articleUpdateRequestDto.getTitle());
        articleUpdateDto.setDescription(articleUpdateRequestDto.getDescription());
        articleUpdateDto.setTradingPlace(articleUpdateRequestDto.getTradingPlace());

        Article article;

        try {
            article = articleRepository.findById(articleId).get();
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }

        if (article.getArticleStatus() == ArticleStatus.HIDE) {
            throw new IsDeleteArticleException();
        }

        if (confirmOwner(article, loginMember)) {
            throw new ForbiddenException();
        }

        List<MultipartFile> multipartFiles = articleUpdateRequestDto.getImageFiles();

        if (multipartFiles == null) {
            throw new NoImageException();
        }

        if (multipartFiles.size() > IMAGE_MAX_RANGE) {
            throw new FileOutOfRangeException();
        }

        // 파일 유효성 검사 실패 시 에러 추가
        if (!multipartFiles.isEmpty()) {
            List<ImageFile> imageFiles = fileStore.storeFiles(multipartFiles);
            articleUpdateDto.setImageFiles(imageFiles);
        }

        article.update(articleUpdateDto, itemUpdateDto);

        if (!articleUpdateDto.getImageFiles().isEmpty()) {
            article.formatImageFiles();

            articleUpdateDto.getImageFiles().stream()
                    .forEach(imageFile -> ImageFile.createImageFile(article, imageFile));
            log.info("------사진 업데이트 성공--------");
        } else if (articleUpdateDto.getImageFiles().isEmpty()) {
            throw new NoImageException();
        }

        articleRepository.save(article);

        List<String> fileNames = article.getImageFiles().stream()
                .map(imageFile -> getFullPath(imageFile.getStoreFileName()))
                .collect(Collectors.toList());

        return new ArticleUpdateResponseDto(
                article.getMember().getUuid(),
                article.getTitle(),
                article.getDescription(),
                article.getTradingPlace(),
                article.getItem().getItemName(),
                article.getItem().getPrice(),
                fileNames);
    }

    public boolean confirmOwner(Article article, Member member) {
        if (article.getMember().getId() == member.getId()) {
            return false;
        }
        return true;
    }

    private String getFullPath(String filename) {
        return FILE_URL + filename;
    }
}
