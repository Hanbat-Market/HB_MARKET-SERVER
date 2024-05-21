package HM.Hanbat_Market.service.account.jwt;

import HM.Hanbat_Market.exception.ErrorResult;
import HM.Hanbat_Market.exception.account.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: ", authException);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResult errorResult;
        if (authException instanceof TokenExpiredException) {
            errorResult = new ErrorResult(401, "UNAUTHORIZED_TOKEN_EXPIRED", "만료된 토큰입니다.");
        } else {
            errorResult = new ErrorResult(401, "UNAUTHORIZED", "인증이 필요합니다.");
        }

        new ObjectMapper().writeValue(response.getOutputStream(), errorResult);
    }
}
