package com.project.core.domain.credentials;

import reactor.core.publisher.Mono;
import java.util.UUID;

public interface CredentialsNode {

    Mono<UUID> signUp(String login, String password);
}
