package HM.Hanbat_Market.exception.article;

import HM.Hanbat_Market.exception.ApplicationException;

public class IsDeleteArticleException extends ApplicationException {
    private final static int STATUS = 400;
    private final static String ERROR_CODE = "BAD_REQUEST_FILE";
    private final static String ERROR_MESSAGE = "삭제된 게시글 입니다.";

    public IsDeleteArticleException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }
}
