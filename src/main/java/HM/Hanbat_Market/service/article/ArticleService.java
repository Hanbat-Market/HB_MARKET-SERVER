package HM.Hanbat_Market.service.article;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.article.dto.ArticleUpdateDto;
import HM.Hanbat_Market.repository.article.dto.ImageFileDto;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    //영속성 전이로 ItemRepository에 따로 persist하지 않아도 됨
    @Transactional
    public Long regisArticle(Long memberId, ArticleCreateDto articleCreateDto, ItemCreateDto itemCreateDto) {
        Member newMember = memberRepository.findById(memberId).get();
        Item newItem = Item.createItem(itemCreateDto, newMember);
        articleCreateDto.setItem(newItem);
        articleCreateDto.setMember(newMember);
        return articleRepository.save(Article.create(articleCreateDto)).getId();
    }

    @Transactional
    public Long regisArticle(Long memberId, ArticleCreateDto articleCreateDto, ItemCreateDto itemCreateDto, List<ImageFileDto> imageFilesDto) {
        Member newMember = memberRepository.findById(memberId).get();
        Item newItem = Item.createItem(itemCreateDto, newMember);
        articleCreateDto.setItem(newItem);
        articleCreateDto.setMember(newMember);

        Article newArticle = Article.create(articleCreateDto);

        imageFilesDto.stream()
                .forEach(imageFileDto -> ImageFile
                        .createImageFile(newArticle, imageFileDto));

        return articleRepository.save(newArticle).getId();
    }

    //검색
    public List<Article> findArticles(ArticleSearchDto articleSearchDto) {
        return articleRepository.findAllBySearch(articleSearchDto);
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
