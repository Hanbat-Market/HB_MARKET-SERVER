package HM.Hanbat_Market.service.account.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
public class JWTUtil {

    private SecretKey secretKey;
    public static final String TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "AuthorizationRefresh";

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUUID(String token) throws ExpiredJwtException{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("UUID", String.class);
    }

    public String getUsername(String token) throws ExpiredJwtException{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("mail", String.class);
    }

    public String getTokenType(String token) throws ExpiredJwtException{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("type", String.class);
    }

    public String getEmail(String token) throws ParseException, JOSEException {
        // JWT 파싱
        SignedJWT signedJWT = SignedJWT.parse(token);

//        // 서명 확인
//        if (!signedJWT.verify()) {
//            throw new JOSEException("Invalid signature");
//        }

        // 클레임 추출
        JWTClaimsSet claimsSet = (JWTClaimsSet) signedJWT.getJWTClaimsSet();
        return claimsSet.getStringClaim("email");
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
                .before(new Date());
    }

    public String createAccessTokenJwt(String uuid, String mail, String role) {

        Long expiredMs = 60000000000L;

        return Jwts.builder()
                .claim("UUID", uuid)
                .claim("mail", mail)
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshTokenJwt(String uuid, String mail, String role) {

        Long expiredMs = 180000L;

        return Jwts.builder()
                .claim("UUID", uuid)
                .claim("mail", mail)
                .claim("role", role)
                .claim("type", "refresh")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Request Header의 Cookie에서 토큰 추출
     */
    public String resolveTokenFromRequest(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (ObjectUtils.isEmpty(cookies)) {
            return null;
        }

        Optional<String> token = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(TOKEN_HEADER))
                .map(c -> c.getValue())
                .findFirst();

        if (token.isPresent()) {
            return token.get();
        }

        return null;
    }
    public String resolveRefreshTokenFromRequest(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (ObjectUtils.isEmpty(cookies)) {
            return null;
        }

        Optional<String> token = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(REFRESH_TOKEN_HEADER))
                .map(c -> c.getValue())
                .findFirst();

        if (token.isPresent()) {
            return token.get();
        }

        return null;
    }
}