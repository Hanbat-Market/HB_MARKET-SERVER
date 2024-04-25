package HM.Hanbat_Market.service.member;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.JoinException;
import HM.Hanbat_Market.exception.member.LoginException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public Long login(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        member.login();
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public Long logout(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        member.logout();
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public String updateFcmToken(Long memberId, String fcmToken) {
        Member member = memberRepository.findById(memberId).get();
        String saveFcmToken = member.saveFcmToken(fcmToken);
        memberRepository.save(member);
        return saveFcmToken;
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByNickName((member.getNickname()));
        if (findMember.isPresent()) {
            throw new JoinException();
        }

        findMember = memberRepository.findByMail((member.getMail()));
        if (findMember.isPresent()) {
            throw new JoinException();
        }
    }

    /**
     * 회원 조회
     */

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findOne(String nickName) {
        return memberRepository.findByNickName(nickName);
    }

    public Optional<Member> findByMail(String mail) {
        return memberRepository.findByMail(mail);
    }

    /**
     * 로그인
     */
//    public Member login(String mail, String passwd) {
//        return memberRepository.findByMail(mail)
//                .filter(m -> m.getPasswd().equals(passwd))
//                .orElseThrow(LoginException::new);
//    }
}
