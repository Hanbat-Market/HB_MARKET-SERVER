package HM.Hanbat_Market.service.member;

import HM.Hanbat_Market.api.article.FileStore;
import HM.Hanbat_Market.api.member.dto.ProfileImageResponse;
import HM.Hanbat_Market.api.member.dto.ProfileNicknameRequest;
import HM.Hanbat_Market.api.member.dto.ProfileResponse;
import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.MemberStatus;
import HM.Hanbat_Market.exception.account.AlreadyVerifiedStudentException;
import HM.Hanbat_Market.exception.account.FailMailVerificationException;
import HM.Hanbat_Market.exception.account.NotMatchingUuidAndRandomNumberException;
import HM.Hanbat_Market.exception.account.UnverifiedStudentException;
import HM.Hanbat_Market.exception.member.JoinException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.APIURL;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileStore fileStore;

    private final String FILE_URL = APIURL.url;

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
    public Long verification(String uuid, String number) {
        Member member = memberRepository.findByUUID(uuid).get();
        if (member.getVerificationNumber() != number) {
            throw new NotMatchingUuidAndRandomNumberException();
        }

        if (member.getMemberStatus().equals(MemberStatus.VERIFIED)) {
            throw new AlreadyVerifiedStudentException();
        }
        if (member.verification(number)) {
            member.verificationSuccess();
            memberRepository.save(member);
            return member.getId();
        }
        //인증실패
        throw new FailMailVerificationException();
    }

    @Transactional
    public Long regisVerificationNumber(String uuid, String number) {
        Member member = memberRepository.findByUUID(uuid).get();
        member.regisVerificationNumber(number);
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

    public boolean isVerifiedStudent(String uuid) {
        Member member = memberRepository.findByUUID(uuid).get();
        if (member.getMemberStatus().equals(MemberStatus.VERIFIED)) {
            return true;
        }
        throw new UnverifiedStudentException();
    }

    @Transactional
    public ProfileImageResponse regisProfileImage(MultipartFile newImageFile, String memberUuid) throws IOException {
        Member member = memberRepository.findByUUID(memberUuid).get();

        ImageFile profileImage = fileStore.storeFile(newImageFile);
        member.setImageFile(profileImage);

        memberRepository.save(member);

        return new ProfileImageResponse(member.getMail(), member.getImageFile().getStoreFileName());
    }

    public ProfileResponse getProfileDetail(String uuid) {
        Member member = memberRepository.findByUUID(uuid).get();
        return new ProfileResponse(member.getMail(), member.getNickname(),
                getFullPath(member.getImageFile().getStoreFileName()));
    }

    public void setProfileNickname(ProfileNicknameRequest profileNicknameRequest) {
        Member member = memberRepository.findByUUID(profileNicknameRequest.getUuid()).get();
        member.updateNickname(profileNicknameRequest.getNickName());
        memberRepository.save(member);
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

    private String getFullPath(String filename) {
        return FILE_URL + filename;
    }
}
