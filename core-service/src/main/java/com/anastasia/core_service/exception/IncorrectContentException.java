package com.anastasia.core_service.exception;

public class IncorrectContentException extends RuntimeException {

    public IncorrectContentException(String message) {
        super(message);
    }

    public IncorrectContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
