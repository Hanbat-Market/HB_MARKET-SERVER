package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class NoHanbatMailException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_WRONG_NO_HANBAT_MAIL";
    private final static String ERROR_MESSAGE = "한밭대학교 계정이 아닙니다.";

    public NoHanbatMailException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}

