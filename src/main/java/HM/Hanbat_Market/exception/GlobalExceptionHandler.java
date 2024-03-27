package HM.Hanbat_Market.exception;

import HM.Hanbat_Market.exception.article.FileOutOfRangeException;
import HM.Hanbat_Market.exception.article.FileValidityException;
import HM.Hanbat_Market.exception.article.IsDeleteArticleException;
import HM.Hanbat_Market.exception.article.NoImageException;
import HM.Hanbat_Market.exception.member.AlreadyLoginException;
import HM.Hanbat_Market.exception.member.JoinException;
import HM.Hanbat_Market.exception.member.LoginException;
import HM.Hanbat_Market.exception.member.UnAuthorizedException;
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
}