package com.anastasia.core_service.exception;

public class NotFoundException extends RuntimeException {

    private static final String BY_ID = "The requested object (%s) with the ID=%s was not found";

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException byID(Class<?> type, Object ID) {
        String message = BY_ID.formatted(type.getSimpleName(), ID);
        return new NotFoundException(message);
    }
}
