package com.anastasia.core_service.service;

import com.anastasia.core_service.entity.user.TelegramChat;
import com.anastasia.core_service.entity.user.User;
import com.anastasia.trade_project.enums.Language;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UserDataService {

    Mono<User> create(User user);

    Mono<TelegramChat> createTelegramChat(TelegramChat telegramChat);

    Mono<User> getById(UUID id);

    Mono<TelegramChat> getTelegramChatById(Long chatId);

    Mono<TelegramChat> getTelegramChatByUser(User user);

    Mono<Void> updateName(UUID id, String name);

    Mono<Void> updateLanguage(UUID id, Language language);
}
