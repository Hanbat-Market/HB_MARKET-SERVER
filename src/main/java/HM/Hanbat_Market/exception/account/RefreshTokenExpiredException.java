package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class RefreshTokenExpiredException extends ApplicationException {
    private final static int STATUS = 401;
    private final static String ERROR_CODE = "UNAUTHORIZED_REFRESH_TOKEN_EXPIRED";
    private final static String ERROR_MESSAGE = "만료된 리프레시 토큰입니다.";

    public RefreshTokenExpiredException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}