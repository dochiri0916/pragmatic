package com.dochiri.pragmatic.domain.common.exception;

import lombok.Getter;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class BaseException extends ErrorResponseException {

    private final ErrorCode errorCode;
    private final Map<String, Object> properties;

    public BaseException(ErrorCode errorCode, Object... args) {
        this(requireErrorCode(errorCode), mapArgs(args));
    }

    private BaseException(ErrorCode errorCode, Map<String, Object> properties) {
        super(errorCode.getHttpStatus(), createBody(errorCode), null);
        this.errorCode = errorCode;
        this.properties = properties;

        getBody().setProperty("code", errorCode.name());

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            getBody().setProperty(entry.getKey(), entry.getValue());
        }
    }

    private static ProblemDetail createBody(ErrorCode errorCode) {
        return ProblemDetail.forStatusAndDetail(errorCode.getHttpStatus(), errorCode.getMessage());
    }

    private static ErrorCode requireErrorCode(ErrorCode errorCode) {
        return Objects.requireNonNull(errorCode, "errorCode는 필수입니다.");
    }

    private static Map<String, Object> mapArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return Map.of();
        }

        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("args는 키/값 쌍이어야 합니다.");
        }

        Map<String, Object> mapped = new LinkedHashMap<>();
        for (int index = 0; index < args.length; index += 2) {
            String key = String.valueOf(args[index]);
            Object value = args[index + 1];
            mapped.put(key, value);
        }
        return Map.copyOf(mapped);
    }

}
