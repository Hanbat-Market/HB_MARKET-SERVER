package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.CreateTestEntity;
import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaImageFileRepositoryTest {
    @Autowired
    ArticleRepository jpaArticleRepository;
    @Autowired
    ImageFileRepository jpaImageFileRepository;
    @Autowired
    MemberService memberService;

    @Test
    public void 아이템_등록() throws Exception {
        //given
        Member newMember = createTestMember1();
        memberService.join(newMember);

        Item testItem = createTestItem(newMember);
        Article article = creteTestArticle(newMember, testItem);
        ImageFile imageFile = ImageFile.createImageFile(article, "jpg", "/");

        //when
        jpaArticleRepository.save(article);

        //then
        List<Article> findArticles = jpaArticleRepository.findAllByMember(newMember);
        assertEquals(imageFile, findArticles.get(0).getImageFiles().get(0));
    }
}