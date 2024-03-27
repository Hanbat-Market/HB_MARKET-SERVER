package HM.Hanbat_Market.exception.trade;

import HM.Hanbat_Market.exception.ApplicationException;

public class OnlyReservationOwnerException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_ONLY_RESERVATION_OWNER";
    private final static String ERROR_MESSAGE = "소유자만 거래를 예약할 수 있습니다.";

    public OnlyReservationOwnerException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}