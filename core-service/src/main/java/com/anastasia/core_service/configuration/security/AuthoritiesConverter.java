package com.anastasia.core_service.configuration.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        return realmRoles(jwt).stream()
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    @SuppressWarnings("unchecked")
    protected List<String> realmRoles(Jwt jwt) {
        var realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null) {
            return new ArrayList<>();
        }
        var roles = (List<String>) realmAccess.get("roles");

        return roles != null ? roles : new ArrayList<>();
    }
}
