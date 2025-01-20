package com.project.core.service;

import com.project.core.entity.Account;
import com.project.enums.Broker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.UUID;

public interface AccountService {

    Mono<Account> create(Account account);

    Mono<Account> getById(UUID id, UUID userId);

    Mono<Account> getByBrokerAndClientId(Broker broker, String clientId, UUID userId);

    Flux<Account> getAllByUserId(UUID userId);

    Mono<Void> updateToken(UUID id, UUID userId, String token, LocalDate tokenExpiresAt);

    Mono<Void> updateRiskProfile(UUID id, UUID userId, UUID riskProfileId);

    Mono<Void> delete(UUID id, UUID userId);
}
