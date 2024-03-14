package HM.Hanbat_Market.exception.member;

import HM.Hanbat_Market.exception.ApplicationException;

public class JoinException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_JOIN";
    private final static String ERROR_MESSAGE = "이미 존재하는 회원입니다.";

    public JoinException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
