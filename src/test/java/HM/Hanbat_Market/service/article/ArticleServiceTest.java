package HM.Hanbat_Market.service.article;

import HM.Hanbat_Market.CreateTestEntity;
import HM.Hanbat_Market.domain.entity.*;
import HM.Hanbat_Market.repository.article.ArticleRepository;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ArticleSearchDto;
import HM.Hanbat_Market.repository.article.dto.ArticleUpdateDto;
import HM.Hanbat_Market.repository.article.dto.ImageFileDto;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArticleServiceTest {
    @Autowired
    ArticleService articleService;
    @Autowired
    MemberService memberService;

    @Test
    public void 게시글_등록() throws Exception {
        //given
        Member member = Member.createMember("jckim229@gmail.com", "0303", "01028564221", "김주찬");
        memberService.join(member);
        ArticleCreateDto articleCreateDto = createArticleCreateDto("PS5 팝니다.", "싸게 팝니다.", "대전");
        ItemCreateDto itemCreateDto = createItemCreateDto("PS5", 170000L);
        List<ImageFileDto> imageFilesDto = createTestImageFilesDto();
        //when
        articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto, imageFilesDto);
        articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto);
        //then (등록된 게시글이 2개)
        assertEquals(2, articleService.findArticles(member).size());
        assertEquals(2, articleService.findArticles().size());

        //등록된 이미지 파일이 없음
        assertEquals(0, articleService.findArticles().get(1).getImageFiles().size());
    }

    @Test
    public void 게시글_등록_검색() throws Exception {
        //given
        Member member = Member.createMember("jckim229@gmail.com", "0303", "01028564221", "김주찬");
        memberService.join(member);

        ArticleCreateDto articleCreateDto = createArticleCreateDto("PS5 팝니다.", "싸게 팝니다.", "대전");
        ArticleCreateDto articleCreateDto2 = createArticleCreateDto("PS3 팝니다.", "싸게 팝니다.", "대전");
        ItemCreateDto itemCreateDto = createItemCreateDto("PS5", 170000L);
        ItemCreateDto itemCreateDto2 = createItemCreateDto("PS3", 130000L);
        List<ImageFileDto> imageFilesDto = createTestImageFilesDto();

        ArticleSearchDto articleSearchDto = new ArticleSearchDto();
        articleSearchDto.setTitle("PS3");
        //when
        articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto, imageFilesDto);
        articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto);
        articleService.regisArticle(member.getId(), articleCreateDto2, itemCreateDto2);
        //then (등록된 게시글이 3개)
        assertEquals(3, articleService.findArticles(member).size());
        assertEquals(3, articleService.findArticles().size());

        //then (PS3로 검색한 게시글이 1개)
        assertEquals(1, articleService.findArticles(articleSearchDto).size());
        articleSearchDto.setItemName("PS");
        articleSearchDto.setTitle("");

        //then (PS로 검색한 아이템 이름에 해당하는 게시글이 3개
        assertEquals(3, articleService.findArticles(articleSearchDto).size());

        //등록된 이미지 파일이 없음
        assertEquals(0, articleService.findArticles().get(1).getImageFiles().size());
    }

    @Test
    public void 게시글_수정() throws Exception {
        //given
        Member member = Member.createMember("jckim229@gmail.com", "0303", "01028564221", "김주찬");
        memberService.join(member);

        ArticleCreateDto articleCreateDto = createArticleCreateDto("PS5 팝니다.", "싸게 팝니다.", "대전");
        ItemCreateDto itemCreateDto = createItemCreateDto("PS5", 170000L);
        List<ImageFileDto> imageFilesDto = createTestImageFilesDto();

        Long articleId = articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto);
        Article article = articleService.findArticle(articleId);

        //when
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle("수정한 게시글 제목");
        articleUpdateDto.setDescription("qwer");
        articleUpdateDto.setItemUpdateDto(createItemUpdateDto());
        articleUpdateDto.setTradingPlace("sdf");
        imageFilesDto.stream()
                .forEach(imageFileDto -> ImageFile.createImageFile(article, imageFileDto));

        articleService.updateArticle(articleId, articleUpdateDto);

        //then (수정된 게시글 제목으로 검색)
        ArticleSearchDto articleSearchDto = new ArticleSearchDto();
        articleSearchDto.setTitle("수정한");
        assertEquals(1, articleService.findArticles(articleSearchDto).size());

        //then (등록한 이미지 파일 2개)
        assertEquals(2, articleService.findArticles().get(0).getImageFiles().size());

    }

    @Test
    public void 게시글_삭제() throws Exception {
        //given
        Member member = Member.createMember("jckim229@gmail.com", "0303", "01028564221", "김주찬");
        memberService.join(member);

        ArticleCreateDto articleCreateDto = createArticleCreateDto("PS5 팝니다.", "싸게 팝니다.", "대전");
        ItemCreateDto itemCreateDto = createItemCreateDto("PS5", 170000L);

        Long articleId = articleService.regisArticle(member.getId(), articleCreateDto, itemCreateDto);

        //when (상태를 변경하는 것으로 softDelete 진행
        articleService.deleteArticle(articleId);

        //then (삭제 후 전체 게시글의 수가 0
        assertEquals(0, articleService.findArticles().size());
    }

}