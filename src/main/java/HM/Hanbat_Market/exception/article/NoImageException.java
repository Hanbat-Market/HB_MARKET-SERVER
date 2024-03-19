package HM.Hanbat_Market.exception.article;


import HM.Hanbat_Market.exception.ApplicationException;

public class NoImageException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_NO_IMAGE";
    private final static String ERROR_MESSAGE = "이미지 파일은 필수입니다.";

    public NoImageException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
