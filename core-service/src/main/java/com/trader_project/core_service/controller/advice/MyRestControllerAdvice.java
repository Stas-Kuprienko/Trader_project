package com.trader_project.core_service.controller.advice;

import com.trader_project.core_service.exception.InternalServiceException;
import com.trade_project.exception.NotFoundException;
import com.trade_project.exception.PaginationException;
import com.trade_project.forms.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class MyRestControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ErrorDto>> validationExceptionHandle(MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage());
        StringBuilder errorStr = new StringBuilder();
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        for (int i = 0; i < errors.size(); i++) {
            FieldError error = errors.get(i);
            errorStr.append(error.getField()).append(" ").append(error.getDefaultMessage());
            if (i != errors.size() - 1) { errorStr.append(", "); }
        }
        return createForHttpStatus(HttpStatus.BAD_REQUEST, errorStr.toString());
    }


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


    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ResponseEntity<ErrorDto>> duplicateKeyHandle(DuplicateKeyException exception) {
        log.info(exception.getMessage());
        return createForHttpStatus(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
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
