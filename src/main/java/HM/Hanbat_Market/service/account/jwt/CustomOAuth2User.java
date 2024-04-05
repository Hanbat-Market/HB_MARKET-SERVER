package HM.Hanbat_Market.service.account.jwt;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final Member member;

    public CustomOAuth2User(Member userDTO) {

        this.member = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return Role.USER.getKey();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return member.getName();
    }

    public String getUsername() {

        return member.getMail();
    }
}