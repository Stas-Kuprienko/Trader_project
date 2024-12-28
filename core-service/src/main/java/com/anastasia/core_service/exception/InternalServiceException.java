package com.anastasia.core_service.exception;

public class InternalServiceException extends RuntimeException {

    private static final String KEYCLOAK_CREATION_FAIL = "Couldn't register user in Keycloak: %s (Cause: %s)";
    private static final String UNEXPECTED_DATA = "Unexpected data from %s: ";

    public InternalServiceException(String message) {
        super(message);
    }


    public static InternalServiceException keycloakAuthFail(String login, String cause) {
        String message = KEYCLOAK_CREATION_FAIL.formatted(login, cause);
        return new InternalServiceException(message);
    }

    public static InternalServiceException unexpectedData(String from, Object data) {
        String message = UNEXPECTED_DATA.formatted(from) + data;
        return new InternalServiceException(message);
    }
}
