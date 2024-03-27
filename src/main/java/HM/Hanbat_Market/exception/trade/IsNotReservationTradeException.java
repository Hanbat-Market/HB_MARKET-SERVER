package HM.Hanbat_Market.exception.trade;

import HM.Hanbat_Market.exception.ApplicationException;

public class IsNotReservationTradeException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_CANNOT_CANCEL_TRADE";
    private final static String ERROR_MESSAGE = "예약된 거래만 취소할 수 있습니다.";

    public IsNotReservationTradeException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}