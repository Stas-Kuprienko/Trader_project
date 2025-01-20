package com.project.core.utility;

import com.project.core.exception.InternalServiceException;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.UUID;

public final class JwtUtility {

    private JwtUtility() {}


    public static String extractEmail(Jwt jwt) {
        String email = (String) jwt.getClaims().get("email");
        if (email == null) {
            throw InternalServiceException.jwtClaimsNotFound(jwt, "Email");
        } else {
            return email;
        }
    }


    public static UUID extractUserId(Jwt jwt) {
        String userId = (String) jwt.getClaims().get("sub");
        if (userId == null) {
            throw InternalServiceException.jwtClaimsNotFound(jwt, "ID");
        } else {
            return UUID.fromString(userId);
        }
    }
}
