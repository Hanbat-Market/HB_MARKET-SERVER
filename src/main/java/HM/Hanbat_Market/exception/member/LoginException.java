package HM.Hanbat_Market.exception.member;

import HM.Hanbat_Market.exception.ApplicationException;

public class LoginException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_LOGIN";
    private final static String ERROR_MESSAGE = "아이디 비밀번호가 일치하지 않습니다.";

    public LoginException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
