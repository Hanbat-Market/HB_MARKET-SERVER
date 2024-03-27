package HM.Hanbat_Market.service.member;

import HM.Hanbat_Market.api.member.login.Login;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.JoinException;
import HM.Hanbat_Market.exception.member.LoginException;
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

        JoinException e = assertThrows(JoinException.class, ()
                -> memberService.join(member2));

        //변경 감지로 인한 update
        member1.changeMail("123@gmail.com");
        Member findMember1 = jpaMemberRepository.findById(member1.getId()).get();

        assertEquals(1, jpaMemberRepository.findAll().size());
        assertEquals("123@gmail.com", findMember1.getMail());
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

        JoinException e = assertThrows(JoinException.class, ()
                -> memberService.join(member2));
    }

    @Test
    public void 로그인_실패() {
        //given

        //when, then

        LoginException e = assertThrows(LoginException.class, ()
                -> memberService.login("fail@email.com", "failPasswd"));
    }
}