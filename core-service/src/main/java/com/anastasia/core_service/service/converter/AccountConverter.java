package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.entity.user.Account;
import com.anastasia.core_service.entity.user.RiskProfile;
import com.anastasia.core_service.entity.user.User;
import com.anastasia.core_service.service.UserDataService;
import com.anastasia.trade_project.dto.AccountDto;
import com.anastasia.trade_project.dto.RiskProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collection;
import java.util.UUID;

@Service
public class AccountConverter implements Converter<Account, AccountDto>, CollectionConverter<Account, AccountDto> {

    private final UserDataService userDataService;
    private final RiskProfileConverter riskProfileConverter;

    @Autowired
    public AccountConverter(UserDataService userDataService, RiskProfileConverter riskProfileConverter) {
        this.userDataService = userDataService;
        this.riskProfileConverter = riskProfileConverter;
    }


    @Override
    public Mono<AccountDto> toDto(Account account) {
        return Mono.zip(Mono.just(
                AccountDto.builder()
                        .id(toStringIfNotNull(account.getId()))
                        .userId(account.getUser() != null ? account.getUser().getId() : null)
                        .broker(account.getBroker())
                        .clientId(account.getClientId())
                        .token(account.getToken())
                        .tokenExpiresAt(localDateToString(account.getTokenExpiresAt()))
                        .createdAt(localDateToString(account.getCreatedAt()))
                        .updatedAt(localDateTimeToString(account.getUpdatedAt()))
                        .build()),
                riskProfileConverter.toDto(account.getRiskProfile())
        ).map(tuple -> {
            AccountDto accountDto = tuple.getT1();
            RiskProfileDto riskProfileDto = tuple.getT2();
            accountDto.setRiskProfile(riskProfileDto);
            return accountDto;
        });
    }

    @Override
    public Mono<Account> toEntity(AccountDto dto) {
        User user = new ProxyUser(dto.getUserId(), userDataService);
        user.setId(dto.getUserId());
        return Mono.zip(Mono.just(
                Account.builder()
                        .id(dto.getId() != null ? UUID.fromString(dto.getId()) : null)
                        .user(user)
                        .broker(dto.getBroker())
                        .clientId(dto.getClientId())
                        .token(dto.getToken())
                        .tokenExpiresAt(stringToLocalDate(dto.getTokenExpiresAt()))
                        .createdAt(stringToLocalDate(dto.getCreatedAt()))
                        .updatedAt(stringToLocalDateTime(dto.getUpdatedAt()))
                        .build()),
                riskProfileConverter.toEntity(dto.getRiskProfile())
        ).map(tuple -> {
            Account account = tuple.getT1();
            RiskProfile riskProfile = tuple.getT2();
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
