package HM.Hanbat_Market.exception.member;

import HM.Hanbat_Market.exception.ApplicationException;

public class UpdateNicknameException extends ApplicationException {

    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_UPDATE_NICKNAME_DUPLICATION";
    private final static String ERROR_MESSAGE = "이미 존재하는 닉네임입니다.";

    public UpdateNicknameException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
