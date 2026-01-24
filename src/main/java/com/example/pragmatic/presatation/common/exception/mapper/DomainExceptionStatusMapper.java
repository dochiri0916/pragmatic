package com.example.pragmatic.presatation.common.exception.mapper;

import org.springframework.http.HttpStatus;

public interface DomainExceptionStatusMapper {

    boolean supports(RuntimeException exception);

    HttpStatus map(RuntimeException exception);

}