package HM.Hanbat_Market.exception.trade;

import HM.Hanbat_Market.exception.ApplicationException;

public class AlreadyCompleteTradeCantReservationException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_CANNOT_COMPLETE_TRADE";
    private final static String ERROR_MESSAGE = "완료된 거래는 예약할 수 없습니다.";

    public AlreadyCompleteTradeCantReservationException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}