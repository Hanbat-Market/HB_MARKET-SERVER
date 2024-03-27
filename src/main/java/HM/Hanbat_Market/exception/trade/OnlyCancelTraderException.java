package HM.Hanbat_Market.exception.trade;

import HM.Hanbat_Market.exception.ApplicationException;

public class OnlyCancelTraderException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_ONLY_CANCEL_TRADER";
    private final static String ERROR_MESSAGE = "상품의 거래자들만이 거래를 취소할 수 있습니다.";

    public OnlyCancelTraderException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}