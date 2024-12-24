package com.anastasia.core_service.service.impl;

import com.anastasia.core_service.datasource.jpa.AccountRepository;
import com.anastasia.core_service.entity.user.Account;
import com.anastasia.core_service.exception.DataPersistenceException;
import com.anastasia.core_service.exception.NotFoundException;
import com.anastasia.core_service.service.AccountService;
import com.anastasia.trade_project.enums.Broker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Mono<Account> create(Account account) {
        if (account.getId() != null) {
            throw DataPersistenceException.entityToSaveHasId(account);
        }
        LocalDate createdAt = LocalDate.now();
        account.setCreatedAt(createdAt);
        return accountRepository.save(account);
    }

    @Override
    public Mono<Account> getById(UUID id, Long userId) {
        return accountRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(userId))
                .switchIfEmpty(Mono.error(NotFoundException.byID(Account.class, id)));
    }

    @Override
    public Mono<Account> getByBrokerAndClientId(Broker broker, String clientId, Long userId) {
        return accountRepository.findByBrokerAndClientId(broker, clientId)
                .filter(a -> a.getUser().getId().equals(userId))
                .switchIfEmpty(Mono.error(NotFoundException.byParameters(Account.class, Map.of(
                        "broker", broker,
                        "clientId", clientId))));
    }

    @Override
    public Mono<Void> updateToken(UUID id, String token, LocalDate tokenExpiresAt) {
        LocalDateTime updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return accountRepository.updateToken(id, token, tokenExpiresAt, updatedAt);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return accountRepository.deleteById(id);
    }
}
