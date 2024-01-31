package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.member.JpaMemberRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaItemRepositoryTest {

    @Autowired ItemRepository jpaItemRepository;
    @Autowired MemberRepository jpaMemberRepository;

    @Test
    public void 아이템_등록() throws Exception {
        //given
        Member member = createTestMember();
        Item item = createTestItem(member);
        //when
        jpaItemRepository.save(item);
        //then
        assertEquals(item, jpaItemRepository.findById(item.getId()).get());
    }

    @Test
    public void 아이템_멤버_테스트() throws Exception {
        //given
        Member member = createTestMember();
        jpaMemberRepository.save(member);

        Item item = createTestItem(member);
        jpaItemRepository.save(item);
        //when
        Member member2 = item.getMember();
        //then
        assertEquals(member2, jpaMemberRepository.findById(member.getId()).get());
    }

    /**
     * 모든 아이템을 조회하는 것을 테스트 합니다.
     * 멤버가 갖고 있는 아이템의 수 전체 조회한 아이템의 수를 비교합니다.
     * 멤버가 갖고 있는 아이템의 수와 멤버의 아이템 수를 조회하여 비교합니다.
     */

    @Test
    public void 아이템_전체조회() throws Exception {
        //given
        Member member = createTestMember();
        jpaMemberRepository.save(member);

        Item item = createTestItem(member);
        Item item2 = createTestItem(member);
        Item item3 = createTestItem(member);
        jpaItemRepository.save(item);
        jpaItemRepository.save(item2);
        jpaItemRepository.save(item3);

        Member findMember = jpaMemberRepository.findById(member.getId()).get();

        //when
        List<Item> items = jpaItemRepository.findAll();
        List<Item> memberItems = jpaItemRepository.findByMember(findMember);

        //then
        assertEquals(3, items.size());
        assertEquals(findMember.getItems().size(), items.size());
        assertEquals(findMember.getItems().size(), memberItems.size());
    }

    public Member createTestMember() {
        String mail = "jckim229@gmail.com";
        String passwd = "1234";
        String phoneNumber = "010-1234-1234";
        String nickname = "김주찬";

        return Member.createMember(mail, passwd, phoneNumber, nickname);
    }

    public Item createTestItem(Member member) {
        Long price = 10000L;
        String description = "hello";
        String tradingPlace = "성수역 7번 출구";

        return Item.createItem(price, description, tradingPlace, member);
    }
}