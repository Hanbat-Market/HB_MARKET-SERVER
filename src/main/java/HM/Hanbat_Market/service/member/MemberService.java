package HM.Hanbat_Market.service.member;

import HM.Hanbat_Market.domain.entity.Member;
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

    private final MemberRepository jpaMemberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        jpaMemberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = jpaMemberRepository.findByNickName((member.getNickname()));
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        findMember = jpaMemberRepository.findByMail((member.getMail()));
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 메일입니다.");
        }

        findMember = jpaMemberRepository.findByPhoneNumber((member.getPhoneNumber()));
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 전화번호입니다.");
        }
    }

    /**
     * 회원 조회
     */

    public List<Member> findMembers() {
        return jpaMemberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return jpaMemberRepository.findById(memberId);
    }

    public Optional<Member> findOne(String nickName) {
        return jpaMemberRepository.findByNickName(nickName);
    }
}
