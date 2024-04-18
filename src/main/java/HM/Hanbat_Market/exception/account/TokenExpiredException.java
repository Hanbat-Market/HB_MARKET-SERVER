package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class TokenExpiredException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_TOKEN_EXPIRED";
    private final static String ERROR_MESSAGE = "만료된 토큰입니다.";

    public TokenExpiredException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}