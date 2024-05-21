package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;
import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {
    private static final int STATUS = 401;
    private static final String ERROR_CODE = "UNAUTHORIZED_TOKEN_EXPIRED";
    private static final String ERROR_MESSAGE = "만료된 토큰입니다.";

    public TokenExpiredException() {
        super(ERROR_MESSAGE);
    }

    public int getStatus() {
        return STATUS;
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }

    public String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}