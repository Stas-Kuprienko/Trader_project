package com.anastasia.telegram_bot.controller.advice;

import com.anastasia.telegram_bot.exception.UnregisteredUserException;
import com.anastasia.telegram_bot.utils.ChatBotUtility;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

import java.util.Locale;
import static com.anastasia.telegram_bot.controller.advice.TelegramBotExceptionHandler.MessageTextKey.*;

@Slf4j
@Component
public class TelegramBotExceptionHandler {

    private final MessageSource messageSource;
    private final String serviceUrl;


    public TelegramBotExceptionHandler(MessageSource messageSource,
                                       @Value("${project.variables.service-url}") String serviceUrl) {
        this.messageSource = messageSource;
        this.serviceUrl = serviceUrl;
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
        log.info(e.getMessage());
        return buildMessage(update.getMessage(), NOT_FOUND);
    }

    public Mono<SendMessage> illegalArgumentHandle(IllegalArgumentException e, Update update) {
        log.info(e.getMessage());
        return buildMessage(update.getMessage(), ILLEGAL_ARGUMENT);
    }

    public Mono<SendMessage> defaultHandle(Exception e, Update update) {
        log.info(e.getMessage());
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
}
