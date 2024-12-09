package com.anastasia.core_service.controller.advice;

import com.anastasia.trade_project.dto.form.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class MyRestControllerAdvice {


    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorDto>> illegalArgumentHandle(IllegalArgumentException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDto error = new ErrorDto(status.getReasonPhrase(), exception.getMessage());
        return Mono.just(ResponseEntity.status(status).body(error));
    }
}
