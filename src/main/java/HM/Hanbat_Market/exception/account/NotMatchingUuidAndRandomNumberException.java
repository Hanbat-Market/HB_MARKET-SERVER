package HM.Hanbat_Market.exception.account;

import HM.Hanbat_Market.exception.ApplicationException;

public class NotMatchingUuidAndRandomNumberException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_NOT_MATCHING_UUID_RANDOM_NUMBER";
    private final static String ERROR_MESSAGE = "UUID와 인증 번호가 매칭되지 않습니다.";

    public NotMatchingUuidAndRandomNumberException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
