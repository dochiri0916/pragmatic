package com.dochiri.pragmatic.presentation.common.exception.mapper;

import com.dochiri.pragmatic.domain.auth.ExpiredRefreshTokenException;
import com.dochiri.pragmatic.domain.auth.InvalidRefreshTokenException;
import com.dochiri.pragmatic.domain.auth.RefreshTokenException;
import com.dochiri.pragmatic.domain.auth.RefreshTokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenExceptionStatusMapper implements DomainExceptionStatusMapper {

    @Override
    public boolean supports(RuntimeException exception) {
        return exception instanceof RefreshTokenException;
    }

    @Override
    public HttpStatus map(RuntimeException exception) {
        if (exception instanceof RefreshTokenNotFoundException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (exception instanceof InvalidRefreshTokenException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (exception instanceof ExpiredRefreshTokenException) {
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.BAD_REQUEST;
    }

}