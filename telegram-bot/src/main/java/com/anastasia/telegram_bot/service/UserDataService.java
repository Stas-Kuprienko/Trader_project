package com.anastasia.telegram_bot.service;

import com.anastasia.trade_project.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserDataService {

    Mono<UserDto> getUserByChatId(long chatId);
}
