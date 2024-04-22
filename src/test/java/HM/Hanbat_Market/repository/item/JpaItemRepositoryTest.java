package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.Article;
import HM.Hanbat_Market.domain.entity.Item;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.member.JpaMemberRepository;
import HM.Hanbat_Market.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static HM.Hanbat_Market.CreateTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class JpaItemRepositoryTest {

    @Autowired
    ItemRepository jpaItemRepository;
    @Autowired
    MemberRepository jpaMemberRepository;

    @Test
    public void 아이템_등록() throws Exception {
        //given
        Member member = createTestMember1();

        Item item = createTestItem(member);
        //when
        jpaItemRepository.save(item);
        //then
        assertEquals(item, jpaItemRepository.findById(item.getId()).get());
    }

    @Test
    public void 아이템_멤버_연관성테스트() throws Exception {
        //given
        Member member = createTestMember1();
        jpaMemberRepository.save(member);

        Item item = createTestItem(member);
        jpaItemRepository.save(item);

        //when
        Item findItem = jpaItemRepository.findById(item.getId()).get();
        Member findMember = jpaMemberRepository.findById(findItem.getMember().getId()).get();

        //then
        assertEquals(findItem, jpaItemRepository.findById(item.getId()).get());
        assertEquals(findMember, jpaMemberRepository.findById(member.getId()).get());

        assertEquals(item, findItem);
        assertEquals(member, findMember);
    }

    /**
     * 모든 아이템을 조회하는 것을 테스트 합니다.
     * 멤버와 이이템의 연관성을 테스트 합니다.
     */

    @Test
    public void 아이템_전체조회() throws Exception {
        //given
        Member member = createTestMember1();
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
        List<Item> memberItems = jpaItemRepository.findAllByMember(findMember);

        //then
        assertEquals(3, items.size());
        assertEquals(findMember.getItems().size(), items.size());
        assertEquals(findMember.getItems().size(), memberItems.size());
    }
}