package HM.Hanbat_Market.exception.trade;

import HM.Hanbat_Market.exception.ApplicationException;

public class AlreadyCompleteTradeException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_CANNOT_CANCEL_TRADE";
    private final static String ERROR_MESSAGE = "이미 완료된 거래는 취소할 수 없습니다.";

    public AlreadyCompleteTradeException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
