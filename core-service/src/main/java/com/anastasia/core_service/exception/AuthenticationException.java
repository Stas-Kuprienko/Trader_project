package com.anastasia.core_service.exception;

public class AuthenticationException extends RuntimeException {

    private static final String KEYCLOAK_CREATION_FAIL = "Couldn't register user in Keycloak: ";

    public AuthenticationException(String message) {
        super(message);
    }


    public static AuthenticationException keycloakAuthFail(String login) {
        String message = KEYCLOAK_CREATION_FAIL + login;
        return new AuthenticationException(message);
    }
}
