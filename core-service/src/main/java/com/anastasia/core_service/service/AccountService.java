package com.anastasia.core_service.service;

import com.anastasia.core_service.entity.user.Account;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountService {

    Mono<Account> create(Account account);

    Mono<Account> getById(UUID id, Long userId);
}
