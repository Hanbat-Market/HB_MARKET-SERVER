package HM.Hanbat_Market.exception.member;

import HM.Hanbat_Market.exception.ApplicationException;

public class AlreadyLoginException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_ALREADY_LOGIN";
    private final static String ERROR_MESSAGE = "이미 로그인 되어있습니다.";

    public AlreadyLoginException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}