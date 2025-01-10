package com.anastasia.trade_project.exception;

import java.util.Map;

public class NotFoundException extends RuntimeException {

    private static final String BY_ID = "The requested object (%s) with the ID=%s was not found";
    private static final String BY_PARAMETERS = "The requested object (%s) with the %s was not found";


    public NotFoundException(String message) {
        super(message);
    }


    public static NotFoundException byID(Class<?> type, Object ID) {
        String message = BY_ID.formatted(type.getSimpleName(), ID);
        return new NotFoundException(message);
    }

    public static NotFoundException byParameter(Class<?> type, String parameter, Object value) {
        String message = BY_PARAMETERS.formatted(type.getSimpleName(), parameter + '=' + value);
        return new NotFoundException(message);
    }

    public static NotFoundException byParameters(Class<?> type, Map<String, Object> parameters) {
        StringBuilder params = new StringBuilder();
        for (var e : parameters.entrySet()) {
            params.append(e.getKey())
                    .append('=')
                    .append(e.getValue())
                    .append(',');
        }
        params.deleteCharAt(params.length() - 1);
        String message = BY_PARAMETERS.formatted(type.getSimpleName(), params.toString());
        return new NotFoundException(message);
    }
}
