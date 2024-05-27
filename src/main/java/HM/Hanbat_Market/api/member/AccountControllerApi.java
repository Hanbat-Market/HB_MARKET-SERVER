package HM.Hanbat_Market.api.member;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.api.member.dto.LogoutRequest;
import HM.Hanbat_Market.api.member.dto.RefreshTokenResponse;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.account.RefreshTokenExpiredException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/account")
public class AccountControllerApi {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @GetMapping("/refresh")
    public Result refreshToken(HttpServletRequest request
            , HttpServletResponse response) {

        try {
            String refreshToken = jwtUtil.resolveRefreshTokenFromRequest(request);
            if (jwtUtil.isExpired(refreshToken)) {
                throw new RefreshTokenExpiredException();
            }

            String uuid = jwtUtil.getUUID(refreshToken);
            String mail = jwtUtil.getUsername(refreshToken);
            String role = jwtUtil.getRole(refreshToken);
            String accessToken = jwtUtil.createAccessTokenJwt(uuid, mail, role);

            response.addCookie(createCookie("Authorization", accessToken)); //쿠키에 새로운 AccessToken 발급
            response.addCookie(createCookie("AuthorizationRefresh", refreshToken));

            return new Result(new RefreshTokenResponse(accessToken));
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        }
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
