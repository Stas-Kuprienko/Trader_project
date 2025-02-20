package com.project.core.service.impl;

import com.project.core.datasource.jpa.TelegramChatRepository;
import com.project.core.datasource.jpa.UserDataRepository;
import com.project.core.datasource.jpa.UserDataCustomRepository;
import com.project.core.domain.credentials.CredentialsNode;
import com.project.core.entity.TelegramChat;
import com.project.core.entity.User;
import com.project.core.service.UserDataService;
import com.project.exception.NotFoundException;
import com.project.enums.Language;
import com.project.enums.Role;
import com.project.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository userDataRepository;
    private final UserDataCustomRepository userDataCustomRepository;
    private final TelegramChatRepository telegramChatRepository;
    private final CredentialsNode credentialsNode;


    @Autowired
    public UserDataServiceImpl(UserDataRepository userDataRepository,
                               UserDataCustomRepository userDataCustomRepository,
                               TelegramChatRepository telegramChatRepository,
                               CredentialsNode credentialsNode) {
        this.userDataRepository = userDataRepository;
        this.userDataCustomRepository = userDataCustomRepository;
        this.telegramChatRepository = telegramChatRepository;
        this.credentialsNode = credentialsNode;
    }


    @Transactional
    @Override
    public Mono<User> singUp(String login, String password, String name, Language language) {
        return credentialsNode
                .signUp(login, password)
                .map(id -> User.builder()
                        .id(id)
                        .login(login)
                        .name(name)
                        .language(language)
                        .role(Role.USER)
                        .status(Status.ACTIVE)
                        .createdAt(LocalDate.now())
                        .build())
                .flatMap(userDataCustomRepository::insert);
    }

    @Override
    public Mono<TelegramChat> createTelegramChat(TelegramChat telegramChat) {
        LocalDate createdAt = LocalDate.now();
        telegramChat.setCreatedAt(createdAt);
        return telegramChatRepository.save(telegramChat);
    }

    @Override
    public Mono<User> getById(UUID id) {
        return userDataRepository
                .findById(id)
                .filter(user -> user.getStatus() == Status.ACTIVE)
                .switchIfEmpty(Mono.error(NotFoundException.byID(User.class, id)));
    }

    @Override
    public Mono<TelegramChat> getTelegramChatById(Long chatId) {
        return telegramChatRepository
                .findById(chatId)
                .filter(telegramChat -> telegramChat.getStatus() == Status.ACTIVE)
                .switchIfEmpty(Mono.error(NotFoundException.byID(TelegramChat.class, chatId)));
    }

    @Override
    public Mono<TelegramChat> getTelegramChatByUser(UUID userId) {
        return telegramChatRepository
                .findByUserId(userId)
                .filter(telegramChat -> telegramChat.getStatus() == Status.ACTIVE)
                .switchIfEmpty(Mono.error(
                        NotFoundException.byParameter(TelegramChat.class, "userID", userId)));
    }

    @Override
    public Mono<User> update(UUID id, User updatable) {
        return userDataRepository
                .findById(id)
                .flatMap(user -> {
                    setIfNotNull(updatable::getName, user::setName);
                    setIfNotNull(updatable::getLanguage, user::setLanguage);
                    user.setUpdatedAt(dateTimeNow());
                    return userDataRepository.save(user);
                });
    }

    @Override
    public Mono<Void> setStatus(UUID id, Status status) {
        return userDataRepository
                .updateStatus(id, status, dateTimeNow());
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return userDataRepository
                .deleteById(id);
    }


    private LocalDateTime dateTimeNow() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    private <T> void setIfNotNull(Supplier<T> supplier, Consumer<T> consumer) {
        if (supplier.get() != null) {
            consumer.accept(supplier.get());
        }
    }
}
