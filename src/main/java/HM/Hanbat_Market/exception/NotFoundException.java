package HM.Hanbat_Market.exception;

public class NotFoundException extends ApplicationException {

    private final static int STATUS = 404;
    private final static String ERROR_CODE = "NOT_FOUND";
    private final static String ERROR_MESSAGE = "찾을 수 없는 페이지 혹은 상품입니다.";

    public NotFoundException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}