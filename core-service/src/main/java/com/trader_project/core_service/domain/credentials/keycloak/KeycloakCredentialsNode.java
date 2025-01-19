package com.trader_project.core_service.domain.credentials.keycloak;

import com.trader_project.core_service.domain.credentials.CredentialsNode;
import com.trader_project.core_service.exception.InternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
public class KeycloakCredentialsNode implements CredentialsNode {

    private final RealmResource realmResource;

    @Autowired
    public KeycloakCredentialsNode(RealmResource realmResource) {
        this.realmResource = realmResource;
    }


    @Override
    public Mono<UUID> signUp(String login, String password) {
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
                    log.info(response.getHeaders().toString());
                    String statusInfo = response.getStatusInfo().toString();
                    response.close();

                    if (status == 201) {
                        String id = realmResource
                                .users()
                                .searchByEmail(login, true)
                                .getFirst()
                                .getId();
                        return UUID.fromString(id);
                    } else if (status == 409) {
                        throw new IllegalArgumentException("User duplication: " + login);
                    } else {
                        throw InternalServiceException.keycloakAuthFail(login, statusInfo);
                    }
                });
    }


}
