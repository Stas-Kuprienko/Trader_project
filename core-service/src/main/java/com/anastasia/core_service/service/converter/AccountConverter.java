package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.entity.Account;
import com.anastasia.core_service.entity.RiskProfile;
import com.anastasia.core_service.entity.User;
import com.anastasia.trade_project.dto.AccountDto;
import com.anastasia.trade_project.dto.RiskProfileDto;
import com.anastasia.trade_project.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collection;

@Service
public class AccountConverter implements Converter<Account, AccountDto>, CollectionConverter<Account, AccountDto> {

    private final UserConverter userConverter;
    private final RiskProfileConverter riskProfileConverter;


    @Autowired
    public AccountConverter(UserConverter userConverter, RiskProfileConverter riskProfileConverter) {
        this.userConverter = userConverter;
        this.riskProfileConverter = riskProfileConverter;
    }


    @Override
    public Mono<AccountDto> toDto(Account account) {
        return Mono.zip(Mono.just(
                AccountDto.builder()
                        .id(account.getId())
                        .broker(account.getBroker())
                        .clientId(account.getClientId())
                        .tokenExpiresAt(localDateToString(account.getTokenExpiresAt()))
                        .createdAt(localDateToString(account.getCreatedAt()))
                        .updatedAt(localDateTimeToString(account.getUpdatedAt()))
                        .build()),
                userConverter.toDto(account.getUser()),
                riskProfileConverter.toDto(account.getRiskProfile())
        ).map(tuple -> {
            AccountDto accountDto = tuple.getT1();
            UserDto userDto = tuple.getT2();
            RiskProfileDto riskProfileDto = tuple.getT3();
            accountDto.setUser(userDto);
            accountDto.setRiskProfile(riskProfileDto);
            return accountDto;
        });
    }

    @Override
    public Mono<Account> toEntity(AccountDto dto) {
        return Mono.zip(Mono.just(
                Account.builder()
                        .id(dto.getId())
                        .broker(dto.getBroker())
                        .clientId(dto.getClientId())
                        .tokenExpiresAt(stringToLocalDate(dto.getTokenExpiresAt()))
                        .createdAt(stringToLocalDate(dto.getCreatedAt()))
                        .updatedAt(stringToLocalDateTime(dto.getUpdatedAt()))
                        .build()),
                userConverter.toEntity(dto.getUser()),
                riskProfileConverter.toEntity(dto.getRiskProfile())
        ).map(tuple -> {
            Account account = tuple.getT1();
            User user = tuple.getT2();
            RiskProfile riskProfile = tuple.getT3();
            account.setUser(user);
            account.setRiskProfile(riskProfile);
            return account;
        });
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
}
