package com.anastasia.core_service.exception;

public class NotFoundException extends RuntimeException {

    private static final String BY_ID = "The requested object (%s) with the ID=%s was not found";
    private static final String BY_PARAMETER = "The requested object (%s) with the %s=%s was not found";


    public NotFoundException(String message) {
        super(message);
    }


    public static NotFoundException byID(Class<?> type, Object ID) {
        String message = BY_ID.formatted(type.getSimpleName(), ID);
        return new NotFoundException(message);
    }

    public static NotFoundException byParameter(Class<?> type, String parameter, Object value) {
        String message = BY_PARAMETER.formatted(type.getSimpleName(), parameter, value);
        return new NotFoundException(message);
    }
}
