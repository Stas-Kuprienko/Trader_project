package com.trader_project.telegram_bot.service;

import com.trade_project.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserDataService {

    Mono<UserDto> getUserByChatId(long chatId);
}
