package com.anastasia.core_service.service.impl;

import com.anastasia.core_service.datasource.jpa.AccountRepository;
import com.anastasia.core_service.entity.Account;
import com.anastasia.core_service.exception.DataPersistenceException;
import com.anastasia.core_service.exception.NotFoundException;
import com.anastasia.core_service.service.AccountService;
import com.anastasia.core_service.utility.CryptoUtility;
import com.anastasia.trade_project.enums.Broker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CryptoUtility cryptoUtility;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, CryptoUtility cryptoUtility) {
        this.accountRepository = accountRepository;
        this.cryptoUtility = cryptoUtility;
    }


    @Override
    public Mono<Account> create(Account account) {
        if (account.getId() != null) {
            throw DataPersistenceException.entityToSaveHasId(account);
        }
        String encryptedToken = cryptoUtility.encrypt(account.getToken());
        account.setToken(encryptedToken);
        LocalDate createdAt = LocalDate.now();
        account.setCreatedAt(createdAt);
        return accountRepository.save(account);
    }

    @Override
    public Mono<Account> getById(UUID id, UUID userId) {
        return accountRepository.findById(id)
                .filter(account -> account.getUserId().equals(userId))
                .doOnNext(account -> account.setToken(cryptoUtility.decrypt(account.getToken())))
                .switchIfEmpty(Mono.error(NotFoundException.byID(Account.class, id)));
    }

    @Override
    public Mono<Account> getByBrokerAndClientId(Broker broker, String clientId, UUID userId) {
        return accountRepository.findByBrokerAndClientId(broker, clientId)
                .filter(a -> a.getUserId().equals(userId))
                .doOnNext(account -> account.setToken(cryptoUtility.decrypt(account.getToken())))
                .switchIfEmpty(Mono.error(NotFoundException.byParameters(Account.class, Map.of(
                        "broker", broker,
                        "clientId", clientId))));
    }

    @Override
    public Flux<Account> getAllByUserId(UUID userId) {
        return accountRepository
                .findAllByUserId(userId)
                .doOnNext(account -> account.setToken(cryptoUtility.decrypt(account.getToken())));
    }

    @Override
    public Mono<Void> updateToken(UUID id, UUID userId, String token, LocalDate tokenExpiresAt) {
        return Mono.just(cryptoUtility.encrypt(token))
                .flatMap(encryptedToken -> accountRepository
                        .updateToken(id, encryptedToken, tokenExpiresAt, dateTimeNow()));
    }

    @Override
    public Mono<Void> updateRiskProfile(UUID id, UUID userId, UUID riskProfileId) {
        return accountRepository.updateRiskProfile(id, riskProfileId, dateTimeNow());
    }

    @Override
    public Mono<Void> delete(UUID id, UUID userId) {
        return accountRepository.deleteById(id);
    }


    private LocalDateTime dateTimeNow() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
