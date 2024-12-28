package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.entity.User;
import com.anastasia.trade_project.dto.UserDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserConverter implements Converter<User, UserDto> {


    @Override
    public Mono<UserDto> toDto(User user) {
        return Mono.just(UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole())
                .language(user.getLanguage())
                .name(user.getName())
                .status(user.getStatus())
                .createdAt(localDateToString(user.getCreatedAt()))
                .updatedAt(localDateTimeToString(user.getUpdatedAt()))
                .build());
    }

    @Override
    public Mono<User> toEntity(UserDto dto) {
        return Mono.just(User.builder()
                .id(dto.getId())
                .login(dto.getLogin())
                .name(dto.getName())
                .role(dto.getRole())
                .language(dto.getLanguage())
                .status(dto.getStatus())
                .createdAt(stringToLocalDate(dto.getCreatedAt()))
                .updatedAt(stringToLocalDateTime(dto.getUpdatedAt()))
                .build());
    }
}
