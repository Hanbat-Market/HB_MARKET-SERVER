package HM.Hanbat_Market.exception.member;

import HM.Hanbat_Market.exception.ApplicationException;

public class NotExistUuidException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_NOT_EXIST_UUID";
    private final static String ERROR_MESSAGE = "UUID가 존재하지 않거나 잘못되었습니다.";

    public NotExistUuidException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}

