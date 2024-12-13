package com.anastasia.telegram_bot.controller.advice;

import com.anastasia.telegram_bot.exception.UnregisteredUserException;
import com.anastasia.telegram_bot.utils.ChatBotUtility;
import com.anastasia.telegram_bot.utils.TwoParamsFunction;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static com.anastasia.telegram_bot.controller.advice.TelegramBotExceptionHandler.MessageTextKey.*;

@Slf4j
@Component
public class TelegramBotExceptionHandler {

    private final MessageSource messageSource;
    private final String serviceUrl;
    private final Map<String, TwoParamsFunction<Throwable, Update, Mono<SendMessage>>> functionMap;


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
        Long chatId = update.getMessage().getChatId();
        String username = ChatBotUtility.getUsername(update.getMessage());
        Locale locale = ChatBotUtility.getLocale(update.getMessage());

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
        return buildMessage(update.getMessage(), NOT_FOUND);
    }

    public Mono<SendMessage> illegalArgumentHandle(IllegalArgumentException e, Update update) {
        log.warn(e.getMessage());
        return buildMessage(update.getMessage(), ILLEGAL_ARGUMENT);
    }

    public Mono<SendMessage> defaultHandle(Throwable e, Update update) {
        log.error(e.getMessage(), e);
        return buildMessage(update.getMessage(), DEFAULT);
    }


    private Mono<SendMessage> buildMessage(Message message, MessageTextKey textKey) {
        Long chatId = message.getChatId();
        Locale locale = ChatBotUtility.getLocale(message);

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


    private <E extends Throwable> Map<String, TwoParamsFunction<E, Update, Mono<SendMessage>>> collectExceptionHandlers() {
        Map<String, TwoParamsFunction<E, Update, Mono<SendMessage>>> map = new HashMap<>();
        map.put(UnregisteredUserException.class.getSimpleName(), ((exception, update) -> this.unregisteredUserHandle((UnregisteredUserException) exception, update)));
        map.put(NotFoundException.class.getSimpleName(), ((exception, update) -> this.notFoundHandle((NotFoundException) exception, update)));
        map.put(IllegalArgumentException.class.getSimpleName(), ((exception, update) -> this.illegalArgumentHandle((IllegalArgumentException) exception, update)));
        map.put(null, (this::defaultHandle));
        return map;
    }
}
