package HM.Hanbat_Market.exception.trade;

import HM.Hanbat_Market.exception.ApplicationException;

public class PurchaserAndSellerIsSameException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_PURCHASER_SELLER_SAME";
    private final static String ERROR_MESSAGE = "구매자와 판매자가 같을 수는 없습니다.";

    public PurchaserAndSellerIsSameException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
