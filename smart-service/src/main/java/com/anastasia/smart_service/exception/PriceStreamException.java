package com.anastasia.smart_service.exception;

public class PriceStreamException extends RuntimeException {

    public PriceStreamException(String message) {
        super(message);
    }

    public PriceStreamException(Throwable cause) {
        super(cause);
    }
}
