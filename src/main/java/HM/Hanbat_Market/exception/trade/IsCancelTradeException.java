package HM.Hanbat_Market.exception.trade;

import HM.Hanbat_Market.exception.ApplicationException;

public class IsCancelTradeException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_IS_CANCEL_TRADE";
    private final static String ERROR_MESSAGE = "취소된 거래는 완료될 수 없습니다.";

    public IsCancelTradeException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}