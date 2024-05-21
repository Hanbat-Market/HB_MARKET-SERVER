package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class AccessTokenExpiredException extends ApplicationException {
    private final static int STATUS = 401;
    private final static String ERROR_CODE = "UNAUTHORIZED_ACCESS_TOKEN_EXPIRED";
    private final static String ERROR_MESSAGE = "만료된 액세스 토큰입니다.";

    public AccessTokenExpiredException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}