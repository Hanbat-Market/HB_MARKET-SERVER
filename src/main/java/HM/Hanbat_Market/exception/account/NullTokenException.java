package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class NullTokenException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_NULL_TOKEN";
    private final static String ERROR_MESSAGE = "토큰이 없습니다.";

    public NullTokenException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}