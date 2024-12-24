package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.entity.user.Account;
import com.anastasia.core_service.entity.user.User;
import com.anastasia.trade_project.dto.AccountDto;
import com.anastasia.trade_project.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class UserConverter implements Converter<User, UserDto> {

    private final AccountConverter accountConverter;

    @Autowired
    public UserConverter(AccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }


    @Override
    public Mono<UserDto> toDto(User user) {
        return Mono.zip(
                        Mono.just(UserDto.builder()
                                .id(user.getId())
                                .login(user.getLogin())
                                .role(user.getRole())
                                .language(user.getLanguage())
                                .name(user.getName())
                                .status(user.getStatus())
                                .createdAt(localDateToString(user.getCreatedAt()))
                                .updatedAt(localDateTimeToString(user.getUpdatedAt()))
                                .build()),
                        accountConverter.toDto(user.getAccounts())
                                .collectList())
                .map(tuple -> {
                    UserDto userDto = tuple.getT1();
                    List<AccountDto> accounts = tuple.getT2();
                    userDto.setAccounts(accounts);
                    return userDto;
                });
    }

    @Override
    public Mono<User> toEntity(UserDto dto) {
        return Mono.zip(Mono.just(
                User.builder()
                        .id(dto.getId())
                        .login(dto.getLogin())
                        .name(dto.getName())
                        .role(dto.getRole())
                        .language(dto.getLanguage())
                        .status(dto.getStatus())
                        .createdAt(stringToLocalDate(dto.getCreatedAt()))
                        .updatedAt(stringToLocalDateTime(dto.getUpdatedAt()))
                        .build()),
                accountConverter.toEntity(dto.getAccounts())
                        .collectList()
        ).map(tuple -> {
            User user = tuple.getT1();
            List<Account> accounts = tuple.getT2();
            user.setAccounts(accounts);
            return user;
        });
    }
}
