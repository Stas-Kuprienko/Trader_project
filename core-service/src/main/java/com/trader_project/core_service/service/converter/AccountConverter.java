package com.trader_project.core_service.service.converter;

import com.trader_project.core_service.entity.Account;
import com.trade_project.dto.AccountDto;
import com.trade_project.forms.NewAccount;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collection;

@Service
public class AccountConverter implements Converter<Account, AccountDto>, CollectionConverter<Account, AccountDto> {


    @Override
    public Mono<AccountDto> toDto(Account account) {
        return Mono.just(
                AccountDto.builder()
                        .id(account.getId())
                        .userId(account.getUserId())
                        .broker(account.getBroker())
                        .clientId(account.getClientId())
                        .tokenExpiresAt(localDateToString(account.getTokenExpiresAt()))
                        .riskProfileId(account.getRiskProfileId())
                        .createdAt(localDateToString(account.getCreatedAt()))
                        .updatedAt(localDateTimeToString(account.getUpdatedAt()))
                        .build());
    }

    @Override
    public Mono<Account> toEntity(AccountDto dto) {
        return Mono.just(
                Account.builder()
                        .id(dto.getId())
                        .userId(dto.getUserId())
                        .broker(dto.getBroker())
                        .clientId(dto.getClientId())
                        .tokenExpiresAt(stringToLocalDate(dto.getTokenExpiresAt()))
                        .riskProfileId(dto.getRiskProfileId())
                        .createdAt(stringToLocalDate(dto.getCreatedAt()))
                        .updatedAt(stringToLocalDateTime(dto.getUpdatedAt()))
                        .build());
    }

    @Override
    public Flux<AccountDto> toDto(Collection<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return Flux.empty();
        } else {
            return Flux.fromIterable(accounts)
                    .flatMap(this::toDto);
        }
    }

    @Override
    public Flux<Account> toEntity(Collection<AccountDto> dtoCollection) {
        if (dtoCollection == null || dtoCollection.isEmpty()) {
            return Flux.empty();
        } else {
            return Flux.fromIterable(dtoCollection)
                    .flatMap(this::toEntity);
        }
    }

    public Mono<Account> toEntity(NewAccount newAccount) {
        return Mono.just(
                Account.builder()
                        .userId(newAccount.getUserId())
                        .broker(newAccount.getBroker())
                        .clientId(newAccount.getClientId())
                        .token(newAccount.getToken())
                        .tokenExpiresAt(stringToLocalDate(newAccount.getTokenExpiresAt()))
                        .build()
        );
    }
}
