package com.trader_project.core_service.domain.credentials;

import reactor.core.publisher.Mono;
import java.util.UUID;

public interface CredentialsNode {

    Mono<UUID> signUp(String login, String password);
}
