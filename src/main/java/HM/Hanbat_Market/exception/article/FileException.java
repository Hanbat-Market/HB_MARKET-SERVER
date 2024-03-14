package HM.Hanbat_Market.exception.article;

import HM.Hanbat_Market.exception.ApplicationException;

public class FileException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_FILE";
    private final static String ERROR_MESSAGE = "이미지 파일이 아닙니다.";

    public FileException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
