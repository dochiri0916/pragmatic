package com.dochiri.pragmatic.presentation.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception exception, HttpServletRequest request) {
        if (exception instanceof ErrorResponse errorResponse) {
            logKnownException(errorResponse.getStatusCode(), request, exception);
            return errorResponse;
        }

        log.error("미처리 예외가 발생했습니다. uri={}, method={}", request.getRequestURI(), request.getMethod(), exception);
        return ErrorResponse.create(exception, HttpStatus.INTERNAL_SERVER_ERROR, "일시적인 오류가 발생했습니다.");
    }

    private void logKnownException(
            HttpStatusCode statusCode,
            HttpServletRequest request,
            Exception exception
    ) {
        if (statusCode.is5xxServerError()) {
            log.error(
                    "예외를 처리했습니다. status={}, uri={}, method={}",
                    statusCode.value(),
                    request.getRequestURI(),
                    request.getMethod(),
                    exception
            );
            return;
        }

        log.warn(
                "예외를 처리했습니다. status={}, uri={}, method={}, message={}",
                statusCode.value(),
                request.getRequestURI(),
                request.getMethod(),
                exception.getMessage()
        );
    }

}