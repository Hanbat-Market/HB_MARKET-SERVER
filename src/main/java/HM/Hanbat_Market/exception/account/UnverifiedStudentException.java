package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class UnverifiedStudentException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_UNVERIFIED_STUDENT";
    private final static String ERROR_MESSAGE = "재학생 인증이 필요한 기능입니다.";

    public UnverifiedStudentException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
