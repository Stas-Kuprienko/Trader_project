package com.anastasia.core_service.domain.credentials;

import com.anastasia.core_service.entity.User;
import com.anastasia.core_service.exception.InternalServiceException;
import com.anastasia.trade_project.enums.Role;
import com.anastasia.trade_project.enums.Status;
import com.anastasia.trade_project.forms.RegistrationForm;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class CredentialsNode {

    private final RealmResource realmResource;

    @Autowired
    public CredentialsNode(RealmResource realmResource) {
        this.realmResource = realmResource;
    }


    public Mono<User> signUp(RegistrationForm registration) {
        return Mono.just(new UserRepresentation())
                .map(representation -> {
                    List<UserRepresentation> users = realmResource.users().list();
                    System.out.println(users);
                    CredentialRepresentation credential = new CredentialRepresentation();
                    credential.setTemporary(false);
                    credential.setValue(registration.getPassword());
                    credential.setType(OAuth2Constants.PASSWORD);

                    representation.setEmail(registration.getLogin());
                    representation.setUsername(registration.getLogin());
                    representation.setCredentials(Collections.singletonList(credential));
                    representation.setEnabled(true);

                    return realmResource.users().create(representation);
                }).map(response -> {
                    int status = response.getStatus();
                    response.close();
                    if (status == 200) {
                        String id = realmResource
                                .users()
                                .searchByEmail(registration.getLogin(), true)
                                .getFirst()
                                .getId();
                        return User.builder()
                                .id(UUID.fromString(id))
                                .login(registration.getLogin())
                                .name(registration.getName())
                                .language(registration.getLanguage())
                                .role(Role.USER)
                                .status(Status.ACTIVE)
                                .createdAt(LocalDate.now())
                                .build();
                    } else {
                        throw InternalServiceException.keycloakAuthFail(registration.getLogin());
                    }
                });
    }


}
