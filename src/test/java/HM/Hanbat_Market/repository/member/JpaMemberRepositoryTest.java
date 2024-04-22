package HM.Hanbat_Market.repository.member;

import HM.Hanbat_Market.CreateTestEntity;
import HM.Hanbat_Market.domain.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class JpaMemberRepositoryTest {
    @Autowired
    MemberRepository jpaMemberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = CreateTestEntity.createTestMember1();
        //when
        Member saveMember = jpaMemberRepository.save(member);
        //then
        assertEquals(saveMember, jpaMemberRepository.findById(saveMember.getId()).get());
        assertEquals(saveMember, jpaMemberRepository.findByNickName(saveMember.getNickname()).get());
    }

    @Test
    public void 전체회원조회() throws Exception {
        //given
        Member member1 = CreateTestEntity.createTestMember1();
        Member member2 = Member.createMember("wncks0303@naver.com", "321", "토마스");
        Member member3 = Member.createMember("wncks0303@sadf.com", "213123", "케빈");

        jpaMemberRepository.save(member1);
        jpaMemberRepository.save(member2);
        jpaMemberRepository.save(member3);
        //when
        List<Member> members = jpaMemberRepository.findAll();
        //then
        assertEquals(3, members.size());
    }
}