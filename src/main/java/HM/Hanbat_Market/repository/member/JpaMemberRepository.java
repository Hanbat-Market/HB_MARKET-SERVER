package HM.Hanbat_Market.repository.member;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByNickName(String nickname) {
        List<Member> result = em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
        return result.stream().findAny();
    }

    public Optional<Member> findByLoginMail(String mail) {
        return findAll().stream()
                .filter(m -> m.getMail().equals(mail))
                .findFirst();
    }

    @Override
    public Optional<Member> findByMail(String mail) {
        List<Member> result = em.createQuery("select m from Member m where m.mail like :mail", Member.class)
                .setParameter("mail", mail)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByUUID(String uuid) {
        List<Member> result = em.createQuery("select m from Member m where m.uuid like :uuid", Member.class)
                .setParameter("uuid", uuid)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        List<Member> result = em.createQuery("select m from Member m where m.phoneNumber like :phoneNumber", Member.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
