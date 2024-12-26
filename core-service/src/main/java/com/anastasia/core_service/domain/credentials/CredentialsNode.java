package com.anastasia.core_service.domain.credentials;

import com.anastasia.core_service.exception.AuthenticationException;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.UUID;

@Component
public class CredentialsNode {

    private final RealmResource realmResource;

    @Autowired
    public CredentialsNode(RealmResource realmResource) {
        this.realmResource = realmResource;
    }


    public Mono<UUID> authenticate(String login, String password) {
        return Mono.just(new UserRepresentation())
                .map(representation -> {

                    CredentialRepresentation credential = new CredentialRepresentation();
                    credential.setTemporary(false);
                    credential.setValue(password);
                    credential.setType(OAuth2Constants.PASSWORD);

                    representation.setEmail(login);
                    representation.setUsername(login);
                    representation.setCredentials(Collections.singletonList(credential));
                    representation.setEnabled(true);

                    return realmResource.users().create(representation);
                }).map(response -> {
                    int status = response.getStatus();
                    response.close();
                    if (status == 200) {
                        String uuid = realmResource
                                .users()
                                .searchByEmail(login, true)
                                .getFirst()
                                .getId();
                        return UUID.fromString(uuid);
                    } else {
                        throw AuthenticationException.keycloakAuthFail(login);
                    }
                });
    }


}
