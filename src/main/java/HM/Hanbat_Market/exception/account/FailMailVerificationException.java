package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class FailMailVerificationException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_WRONG_VERIFICATION_NUMBER";
    private final static String ERROR_MESSAGE = "인증 번호가 일치하지 않습니다.";

    public FailMailVerificationException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}

