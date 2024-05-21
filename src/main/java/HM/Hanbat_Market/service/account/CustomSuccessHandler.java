package HM.Hanbat_Market.service.account;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.JoinException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.CustomOAuth2User;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    public CustomSuccessHandler(JWTUtil jwtUtil, MemberService memberService, MemberRepository memberRepository,
                                RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String mail = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        String uuid = customUserDetails.getUUID();

        String accessToken = jwtUtil.createAccessTokenJwt(uuid, mail, role);
        String refreshToken = jwtUtil.createRefreshTokenJwt(uuid, mail, role);

        Member member = memberRepository.findByUUID(uuid).get();

        Optional<RefreshToken> findToken = refreshTokenService.findByUuid(uuid);

        if (findToken.isPresent()) {
            memberService.updateToken(refreshToken, uuid);
        } else if (!findToken.isPresent()) {
            refreshTokenService.save(new RefreshToken(uuid, refreshToken, member));
        }

        response.addCookie(createCookie("AccessToken", accessToken));
        response.addCookie(createCookie("RefreshToken", refreshToken));
        response.sendRedirect("https://vervet-optimal-crawdad.ngrok-free.app/clear");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}