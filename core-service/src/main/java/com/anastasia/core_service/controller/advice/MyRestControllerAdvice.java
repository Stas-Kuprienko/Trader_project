package com.anastasia.core_service.controller.advice;

import com.anastasia.core_service.exception.InternalServiceException;
import com.anastasia.core_service.exception.NotFoundException;
import com.anastasia.core_service.exception.PaginationException;
import com.anastasia.trade_project.forms.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class MyRestControllerAdvice {


    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorDto>> illegalArgumentHandle(IllegalArgumentException exception) {
        log.warn(exception.getMessage());
        return createForHttpStatus(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(PaginationException.class)
    public Mono<ResponseEntity<ErrorDto>> paginationExceptionHandle(PaginationException exception) {
        log.warn(exception.getMessage(), exception);
        return createForHttpStatus(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ErrorDto>> notFoundHandle(NotFoundException exception) {
        log.info(exception.getMessage());
        return createForHttpStatus(HttpStatus.NOT_FOUND, exception.getMessage());
    }



    @ExceptionHandler(InternalServiceException.class)
    public Mono<ResponseEntity<ErrorDto>> internalHandle(InternalServiceException exception) {
        log.error(exception.getMessage(), exception);
        return createForHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }


    private Mono<ResponseEntity<ErrorDto>> createForHttpStatus(HttpStatus status, String message) {
        ErrorDto error = new ErrorDto(status.getReasonPhrase(), message);
        return Mono.just(ResponseEntity.status(status).body(error));
    }
}
