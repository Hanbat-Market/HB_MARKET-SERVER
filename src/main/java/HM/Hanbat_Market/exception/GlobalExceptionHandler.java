package HM.Hanbat_Market.exception;

import HM.Hanbat_Market.exception.account.*;
import HM.Hanbat_Market.exception.article.FileOutOfRangeException;
import HM.Hanbat_Market.exception.article.FileValidityException;
import HM.Hanbat_Market.exception.article.IsDeleteArticleException;
import HM.Hanbat_Market.exception.article.NoImageException;
import HM.Hanbat_Market.exception.member.*;
import HM.Hanbat_Market.exception.trade.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = {RestController.class})
@Slf4j
public class GlobalExceptionHandler {

    private String BR = "BAD_REQUEST";
    private String NF = "NOT_FOUND";
    private String UA = "UNAUTHORIZED";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JoinException.class)
    public ErrorResult JoinExceptionHandler(JoinException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginException.class)
    public ErrorResult LoginExceptionHandler(LoginException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyLoginException.class)
    public ErrorResult AlreadyLoginExceptionHandler(AlreadyLoginException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResult NFHandler(NotFoundException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    public ErrorResult UAHandler(UnAuthorizedException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResult ForbiddenExceptionHandler(ForbiddenException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileValidityException.class)
    public ErrorResult FileValidityExceptionHandler(FileValidityException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyCompleteTradeException.class)
    public ErrorResult AlreadyCompleteTradeExceptionHandler(AlreadyCompleteTradeException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileOutOfRangeException.class)
    public ErrorResult FileOutOfRangeExceptionHandler(FileOutOfRangeException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoImageException.class)
    public ErrorResult NoImageExceptionHandler(NoImageException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IsDeleteArticleException.class)
    public ErrorResult IsDeleteArticleExceptionHandler(IsDeleteArticleException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IsNotReservationTradeException.class)
    public ErrorResult IsNotReservationTradeExceptionHandler(IsNotReservationTradeException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyCompleteTradeCantReservationException.class)
    public ErrorResult AlreadyCompleteTradeCantReservationExceptionHandler(AlreadyCompleteTradeCantReservationException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OnlyCancelTraderException.class)
    public ErrorResult OnlyCancelTraderExceptionHandler(OnlyCancelTraderException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OnlyCompleteTradeOwnerException.class)
    public ErrorResult OnlyCompleteTradeOwnerExceptionHandler(OnlyCompleteTradeOwnerException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OnlyReservationOwnerException.class)
    public ErrorResult OnlyReservationOwnerExceptionHandler(OnlyReservationOwnerException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PurchaserAndSellerIsSameException.class)
    public ErrorResult PurchaserAndSellerIsSameExceptionHandler(PurchaserAndSellerIsSameException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullTokenException.class)
    public ErrorResult NullTokenExceptionHandler(NullTokenException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenNotValidException.class)
    public ErrorResult TokenNotValidExceptionHandler(TokenNotValidException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResult TokenExpiredExceptionHandler(TokenExpiredException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IsCancelTradeException.class)
    public ErrorResult IsCancelTradeExceptionHandler(IsCancelTradeException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistUuidException.class)
    public ErrorResult NotExistUuidExceptionHandler(NotExistUuidException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyReservationException.class)
    public ErrorResult AlreadyReservationExceptionHandler(AlreadyReservationException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnverifiedStudentException.class)
    public ErrorResult UnverifiedStudentExceptionHandler(UnverifiedStudentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyVerifiedStudentException.class)
    public ErrorResult AlreadyVerifiedExceptionHandler(AlreadyVerifiedStudentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FailMailVerificationException.class)
    public ErrorResult FailMailVerificationExceptionHandler(FailMailVerificationException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoHanbatMailException.class)
    public ErrorResult NoHanbatMailExceptionHandler(NoHanbatMailException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotMatchingUuidAndRandomNumberException.class)
    public ErrorResult NotMatchingUuidAndRandomNumberExceptionHandler(NotMatchingUuidAndRandomNumberException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UpdateNicknameException.class)
    public ErrorResult UpdateNicknameExceptionHandler(UpdateNicknameException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }
}

