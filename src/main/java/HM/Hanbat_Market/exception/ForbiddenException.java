package HM.Hanbat_Market.exception;

public class ForbiddenException extends ApplicationException {
    private final static int STATUS = 403;
    private final static String ERROR_CODE = "Forbidden";
    private final static String ERROR_MESSAGE = "요청 권한이 없습니다.";

    public ForbiddenException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
