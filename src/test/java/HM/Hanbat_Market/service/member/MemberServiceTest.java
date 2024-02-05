package HM.Hanbat_Market.service.member;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static HM.Hanbat_Market.CreateTestEntity.createTestMember;
import static org.junit.jupiter.api.Assertions.*;

/**
 * findMembers, findOne과 같은 Repository를 위임받은 메서드에 대한 테스트는 진행하지 않음
 * 오로지 비즈니스 로직이 있는 task만 테스트를 진행
 */

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository jpaMemberRepository;

    @Test
    public void 중복회원_예외_닉네임() {
        //given
        Member member1 = createTestMember("hello",
                "jckim229@gmail.com", "01012341234");

        Member member2 = createTestMember("hello",
                "jckim229@gmail.com", "01043214231");

        //when, then
        memberService.join(member1);

        //변경 감지로 인한 update
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()
                -> memberService.join(member2));
        assertEquals("이미 존재하는 닉네임입니다.", e.getMessage());

        assertEquals(1, jpaMemberRepository.findAll().size());
    }

    @Test
    public void 중복회원_예외_메일() {
        //given
        Member member1 = createTestMember("hello",
                "jckim229@gmail.com", "01012341234");

        Member member2 = createTestMember("hello123",
                "jckim229@gmail.com", "01043214231");

        //when, then
        memberService.join(member1);

        //변경 감지로 인한 update
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()
                -> memberService.join(member2));
        assertEquals("이미 존재하는 메일입니다.", e.getMessage());

        assertEquals(1, jpaMemberRepository.findAll().size());
    }

    @Test
    public void 중복회원_예외_전화번호() {
        //given
        Member member1 = createTestMember("hello",
                "jckim229@gmail.com", "01012341234");

        Member member2 = createTestMember("hello123",
                "zxcv@gmail.com", "01012341234");

        //when, then
        memberService.join(member1);

        //변경 감지로 인한 update
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()
                -> memberService.join(member2));
        assertEquals("이미 존재하는 전화번호입니다.", e.getMessage());

        assertEquals(1, jpaMemberRepository.findAll().size());
    }
}