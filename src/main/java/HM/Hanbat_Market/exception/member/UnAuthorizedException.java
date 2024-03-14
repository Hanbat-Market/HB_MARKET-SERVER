package HM.Hanbat_Market.exception.member;

import HM.Hanbat_Market.exception.ApplicationException;

public class UnAuthorizedException extends ApplicationException {
    private final static int STATUS = 401;
    private final static String ERROR_CODE = "BAD_REQUEST_UNAUTHORIZED";
    private final static String ERROR_MESSAGE = "로그인이 필요합니다.";

    public UnAuthorizedException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
