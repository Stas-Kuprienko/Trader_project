package com.project.telegram.service;

import com.project.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserDataService {

    Mono<UserDto> getUserByChatId(long chatId);
}
