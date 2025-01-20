package com.project.core.service;

import com.project.core.entity.TelegramChat;
import com.project.core.entity.User;
import com.project.enums.Language;
import com.project.enums.Status;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UserDataService {

    Mono<User> singUp(String login, String password, String name, Language language);

    Mono<TelegramChat> createTelegramChat(TelegramChat telegramChat);

    Mono<User> getById(UUID id);

    Mono<TelegramChat> getTelegramChatById(Long chatId);

    Mono<TelegramChat> getTelegramChatByUser(UUID userId);

    Mono<User> update(UUID id, User updatable);

    Mono<Void> setStatus(UUID id, Status status);

    Mono<Void> delete(UUID id);
}
