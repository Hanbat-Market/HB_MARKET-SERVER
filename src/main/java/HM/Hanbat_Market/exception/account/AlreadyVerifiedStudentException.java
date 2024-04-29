package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class AlreadyVerifiedStudentException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_ALREADY_VERIFIED_STUDENT";
    private final static String ERROR_MESSAGE = "이미 인증된 재학생입니다.";

    public AlreadyVerifiedStudentException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}

