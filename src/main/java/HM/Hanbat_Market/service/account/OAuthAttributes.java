package HM.Hanbat_Market.service.account;

import HM.Hanbat_Market.domain.entity.ImageFile;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // OAuth2User에서 반환하는 사용자 정보는 Map
    // 따라서 값 하나하나를 변환해야 한다.
    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {

        return ofGoogle(userNameAttributeName, attributes);
    }

    // 구글 생성자
    private static OAuthAttributes ofGoogle(String usernameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    // User 엔티티 생성
    public Member toEntity(String oauthPasswd) {
        Member member = Member.createMember(email, name, oauthPasswd, "임시 닉네임" + email, Role.USER);
        return member;
    }
}