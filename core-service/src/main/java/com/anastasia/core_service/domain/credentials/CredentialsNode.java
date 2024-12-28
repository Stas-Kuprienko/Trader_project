package com.anastasia.core_service.domain.credentials;

import com.anastasia.core_service.entity.User;
import com.anastasia.core_service.exception.InternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.Collections;

@Slf4j
@Component
public class CredentialsNode {

    private final RealmResource realmResource;

    @Autowired
    public CredentialsNode(RealmResource realmResource) {
        this.realmResource = realmResource;
    }


    public Mono<User> signUp(User user, String password) {
        String id = user.getId().toString();
        String login = user.getLogin();
        return Mono.just(new UserRepresentation())
                .map(representation -> {

                    CredentialRepresentation credential = new CredentialRepresentation();
                    credential.setTemporary(false);
                    credential.setValue(password);
                    credential.setType(OAuth2Constants.PASSWORD);

                    representation.setId(id);
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
                        return user;
                    } else if (status == 409) {
                        throw new IllegalArgumentException("User duplication: " + login);
                    } else {
                        throw InternalServiceException.keycloakAuthFail(login, statusInfo);
                    }
                });
    }


}
