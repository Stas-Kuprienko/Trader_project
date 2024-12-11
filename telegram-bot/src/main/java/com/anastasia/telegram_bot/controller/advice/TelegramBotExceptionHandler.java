package com.anastasia.telegram_bot.controller.advice;

import com.anastasia.telegram_bot.exception.UnregisteredUserException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Locale;
import static com.anastasia.telegram_bot.controller.advice.TelegramBotExceptionHandler.MessageKey.*;

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


    public SendMessage unregisteredUserHandle(UnregisteredUserException e, Update update) {
        log.info(e.getMessage());
        Long chatId = update.getMessage().getChatId();
        String username = getUsername(update);
        Locale locale = getLocale(update);

        String message = messageSource
                .getMessage(UNREGISTERED.name(), new Object[]{username, serviceUrl}, locale);
        //TODO url link button
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage notFoundHandle(NotFoundException e, Update update) {
        log.info(e.getMessage());
        Long chatId = update.getMessage().getChatId();
        Locale locale = getLocale(update);

        String message = messageSource
                .getMessage(NOT_FOUND.name(), null, locale);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage defaultHandle(Exception e, Update update) {
        log.info(e.getMessage());
        Long chatId = update.getMessage().getChatId();
        Locale locale = getLocale(update);

        String message = messageSource
                .getMessage(DEFAULT.name(), null, locale);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }


    private Locale getLocale(Update update) {
        if (update.hasMessage()) {
            return Locale.of(update.getMessage()
                    .getFrom()
                    .getLanguageCode());
        } else {
            throw new IllegalArgumentException("Message is null");
        }
    }

    private String getUsername(Update update) {
        if (update.hasMessage()) {
            return update.getMessage()
                    .getFrom()
                    .getFirstName();
        } else {
            throw new IllegalArgumentException("Message is null");
        }
    }


    enum MessageKey {
        UNREGISTERED,
        NOT_FOUND,

        DEFAULT
    }
}
