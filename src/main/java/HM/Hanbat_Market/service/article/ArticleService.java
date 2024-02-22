package HM.Hanbat_Market.service.article;

import HM.Hanbat_Market.api.dto.HomeArticlesDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ImageFileRepository imageFileRepository;

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

//    @Transactional
//    public Long regisArticle(Long memberId, ArticleCreateDto articleCreateDto, ItemCreateDto itemCreateDto, List<ImageFileDto> imageFilesDto) {
//        Member newMember = memberRepository.findById(memberId).get();
//        Item newItem = Item.createItem(itemCreateDto, newMember);
//        articleCreateDto.setItem(newItem);
//        articleCreateDto.setMember(newMember);
//
//        Article newArticle = Article.create(articleCreateDto);
//
//        imageFilesDto.stream()
//                .forEach(imageFileDto -> ImageFile
//                        .createImageFile(newArticle, imageFileDto));
//
//        return articleRepository.save(newArticle).getId();
//    }

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
