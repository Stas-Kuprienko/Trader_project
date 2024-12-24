package com.anastasia.core_service.service;

import com.anastasia.core_service.entity.user.Account;
import com.anastasia.trade_project.enums.Broker;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.UUID;

public interface AccountService {

    Mono<Account> create(Account account);

    Mono<Account> getById(UUID id, Long userId);

    Mono<Account> getByBrokerAndClientId(Broker broker, String clientId, Long userId);

    Mono<Void> updateToken(UUID id, String token, LocalDate tokenExpiresAt);

    Mono<Void> delete(UUID id);
}
