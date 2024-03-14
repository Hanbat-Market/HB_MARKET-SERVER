package HM.Hanbat_Market.service.member;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.JoinException;
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

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByNickName((member.getNickname()));
        if (findMember.isPresent()) {
            throw new JoinException();
        }

        findMember = memberRepository.findByMail((member.getMail()));
        if (findMember.isPresent())
        {
            throw new JoinException();
        }

        findMember = memberRepository.findByPhoneNumber((member.getPhoneNumber()));
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
    /**
     * 로그인
     */
    public Member login(String mail, String passwd) {
        return memberRepository.findByMail(mail)
                .filter(m -> m.getPasswd().equals(passwd))
                .orElse(null);
    }
}
