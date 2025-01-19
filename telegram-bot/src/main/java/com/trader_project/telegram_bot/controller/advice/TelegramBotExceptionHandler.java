package com.trader_project.telegram_bot.controller.advice;

import com.trader_project.telegram_bot.exception.UnregisteredUserException;
import com.trader_project.telegram_bot.utils.ChatBotUtility;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import static com.trader_project.telegram_bot.controller.advice.TelegramBotExceptionHandler.MessageTextKey.*;

@Slf4j
@Component
public class TelegramBotExceptionHandler {

    private final MessageSource messageSource;
    private final String serviceUrl;
    private final Map<String, BiFunction<Throwable, Update, Mono<SendMessage>>> functionMap;


    @Autowired
    public TelegramBotExceptionHandler(MessageSource messageSource,
                                       @Value("${project.variables.service-url}") String serviceUrl) {
        this.messageSource = messageSource;
        this.serviceUrl = serviceUrl;
        functionMap = collectExceptionHandlers();
    }


    public Mono<SendMessage> apply(Throwable throwable, Update update) {
        var method = functionMap.get(throwable.getClass().getSimpleName());
        if (method == null) {
            method = functionMap.get(null);
        }
        return method.apply(throwable, update);
    }

    public Mono<SendMessage> unregisteredUserHandle(UnregisteredUserException e, Update update) {
        log.info(e.getMessage());
        Long chatId = ChatBotUtility.getChatId(update);
        String username = ChatBotUtility.getUsername(update);
        Locale locale = ChatBotUtility.getLocale(update);

        String message = messageSource
                .getMessage(UNREGISTERED.name(), new Object[]{username, serviceUrl}, locale);
        //TODO url link button
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return Mono.just(sendMessage);
    }

    public Mono<SendMessage> notFoundHandle(NotFoundException e, Update update) {
        log.warn(e.getMessage());
        Long chatId = ChatBotUtility.getChatId(update);
        Locale locale = ChatBotUtility.getLocale(update);
        return buildMessage(chatId, locale, NOT_FOUND);
    }

    public Mono<SendMessage> illegalArgumentHandle(IllegalArgumentException e, Update update) {
        log.warn(e.getMessage());
        Long chatId = ChatBotUtility.getChatId(update);
        Locale locale = ChatBotUtility.getLocale(update);
        return buildMessage(chatId, locale, ILLEGAL_ARGUMENT);
    }

    public Mono<SendMessage> defaultHandle(Throwable e, Update update) {
        log.error(e.getMessage(), e);
        Long chatId = ChatBotUtility.getChatId(update);
        Locale locale = ChatBotUtility.getLocale(update);
        return buildMessage(chatId, locale, DEFAULT);
    }


    private Mono<SendMessage> buildMessage(long chatId, Locale locale, MessageTextKey textKey) {
        String text = messageSource
                .getMessage(textKey.name(), null, locale);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return Mono.just(sendMessage);
    }


    enum MessageTextKey {
        UNREGISTERED,
        NOT_FOUND,
        ILLEGAL_ARGUMENT,

        DEFAULT
    }


    private <E extends Throwable> Map<String, BiFunction<E, Update, Mono<SendMessage>>> collectExceptionHandlers() {
        Map<String, BiFunction<E, Update, Mono<SendMessage>>> map = new HashMap<>();
        map.put(UnregisteredUserException.class.getSimpleName(), ((exception, update) -> this.unregisteredUserHandle((UnregisteredUserException) exception, update)));
        map.put(NotFoundException.class.getSimpleName(), ((exception, update) -> this.notFoundHandle((NotFoundException) exception, update)));
        map.put(IllegalArgumentException.class.getSimpleName(), ((exception, update) -> this.illegalArgumentHandle((IllegalArgumentException) exception, update)));
        map.put(null, (this::defaultHandle));
        return map;
    }
}
