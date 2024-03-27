package HM.Hanbat_Market.exception.article;

import HM.Hanbat_Market.exception.ApplicationException;

public class FileOutOfRangeException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_FILE_OUT_OF_RANGE";
    private final static String ERROR_MESSAGE = "사진은 최대 5장까지 등록할 수 있습니다.";

    public FileOutOfRangeException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
