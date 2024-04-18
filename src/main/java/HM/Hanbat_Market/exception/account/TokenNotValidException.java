package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class TokenNotValidException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_TOKEN_NOT_VALID";
    private final static String ERROR_MESSAGE = "토큰이 없거나 유효하지 않습니다.";

    public TokenNotValidException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}