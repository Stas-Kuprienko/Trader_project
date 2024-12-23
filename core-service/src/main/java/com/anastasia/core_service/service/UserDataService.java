package com.anastasia.core_service.service;

import com.anastasia.core_service.entity.user.TelegramChat;
import com.anastasia.core_service.entity.user.User;
import reactor.core.publisher.Mono;

public interface UserDataService {

    Mono<User> create(User user);

    Mono<TelegramChat> createTelegramChat(TelegramChat telegramChat);

    Mono<User> getById(Long id);

    Mono<TelegramChat> getTelegramChatById(Long chatId);

    Mono<TelegramChat> getTelegramChatByUser(User user);
}
