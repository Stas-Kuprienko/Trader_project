package com.anastasia.core_service.service.impl;

import com.anastasia.core_service.datasource.jpa.AccountRepository;
import com.anastasia.core_service.entity.user.Account;
import com.anastasia.core_service.exception.NotFoundException;
import com.anastasia.core_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
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
        //TODO
        return accountRepository.save(account);
    }

    @Override
    public Mono<Account> getById(UUID id, Long userId) {
        return accountRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(userId))
                .switchIfEmpty(Mono.error(NotFoundException.byID(Account.class, id)));
    }
}
