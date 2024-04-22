package HM.Hanbat_Market.exception.trade;

import HM.Hanbat_Market.exception.ApplicationException;

public class AlreadyReservationException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_ALREADY_RESERVATION";
    private final static String ERROR_MESSAGE = "이미 예약된 거래입니다.";

    public AlreadyReservationException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
