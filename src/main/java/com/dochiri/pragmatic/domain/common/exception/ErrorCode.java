package com.dochiri.pragmatic.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    INVALID_REFRESH_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 아닙니다."),
    REFRESH_TOKEN_OWNER_MISMATCH(HttpStatus.UNAUTHORIZED, "토큰 소유자가 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 토큰을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}