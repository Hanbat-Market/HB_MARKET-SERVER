package HM.Hanbat_Market;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.article.dto.ArticleCreateDto;
import HM.Hanbat_Market.repository.article.dto.ImageFileDto;
import HM.Hanbat_Market.repository.item.dto.ItemCreateDto;
import HM.Hanbat_Market.repository.item.dto.ItemUpdateDto;

import java.util.ArrayList;
import java.util.List;

public class CreateTestEntity {
    public static Member createTestMember2() {
        String mail = "wncks0303@naver.com";
        String passwd = "1234";
        String phoneNumber = "010-4321-4321";
        String nickname = "토마스";

        return Member.createMember(mail, passwd, phoneNumber, nickname);
    }

    public static Member createTestMember1() {
        String mail = "jckim229@gmail.com";
        String passwd = "1234";
        String phoneNumber = "010-1234-1234";
        String nickname = "김주찬";

        return Member.createMember(mail, passwd, phoneNumber, nickname);
    }

    public static Item createTestItem(Member member, String name, Long price) {
        String itemName = name;
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setItemName(itemName);
        itemCreateDto.setPrice(price);

        return Item.createItem(itemCreateDto, member);
    }


    public static Article creteTestArticle(Member member, Item item) {
        ArticleCreateDto articleCreateDto = new ArticleCreateDto();
        articleCreateDto.setTitle("플레이스테이션5 팝니다.");
        articleCreateDto.setDescription("이러이러 합니다.");
        articleCreateDto.setMember(member);
        articleCreateDto.setItem(item);
        return Article.createArticle(articleCreateDto);
    }

    public static Member createTestMember(String nickname, String mail, String phoneNumber) {
        String passwd = "1234";
        return Member.createMember(mail, passwd, phoneNumber, nickname);
    }

    public static Item createTestItem(Member member) {
        String itemName = "PS5";
        Long price = 10000L;
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setItemName(itemName);
        itemCreateDto.setPrice(price);

        return Item.createItem(itemCreateDto, member);
    }

    public static ItemCreateDto createItemCreateDto(String itemName, Long price) {
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setItemName(itemName);
        itemCreateDto.setPrice(price);

        return itemCreateDto;
    }

    public static ArticleCreateDto createArticleCreateDto(String title, String description, String tradingPlace) {
        ArticleCreateDto articleCreateDto = new ArticleCreateDto();
        articleCreateDto.setTitle(title);
        articleCreateDto.setDescription(description);
        articleCreateDto.setTradingPlace(tradingPlace);
        return articleCreateDto;
    }

    public static List<ImageFileDto> createTestImageFilesDto() {
        List<ImageFileDto> imageFilesDto = new ArrayList<>();
        ImageFileDto imageFileDto1 = new ImageFileDto();
        imageFileDto1.setPath("/sf");
        imageFileDto1.setType("png");
        ImageFileDto imageFileDto2 = new ImageFileDto();
        imageFileDto2.setPath("/asd");
        imageFileDto2.setType("jpg");

        imageFilesDto.add(imageFileDto1);
        imageFilesDto.add(imageFileDto2);

        return imageFilesDto;

    }

    public static ItemUpdateDto createItemUpdateDto() {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setItemName("수정한 아이템 이름");
        itemUpdateDto.setPrice(9999L);

        return itemUpdateDto;
    }
}
