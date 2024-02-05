package HM.Hanbat_Market;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;

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

    public static Item createTestItem(Member member, String name) {
        String itemName = name;
        Long price = 10000L;
        String description = "hello";
        String tradingPlace = "성수역 7번 출구";

        return Item.createItem(itemName, price, member);
    }


    public static Article creteTestArticle(Member member, Item item) {
        String title = "증고책 팝니다";
        String description = "hello";
        String tradingPlace = "성수역 7번 출구";

        return Article.createArticle(title, description, tradingPlace, member, item);
    }

    public static Member createTestMember(String nickname, String mail, String phoneNumber) {
        String passwd = "1234";
        return Member.createMember(mail, passwd, phoneNumber, nickname);
    }

    public static Item createTestItem(Member member) {
        String ItemName = "PS5";
        Long price = 10000L;

        return Item.createItem(ItemName, price, member);
    }
}
