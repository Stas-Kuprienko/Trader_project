package com.project.telegram.domain.command.impl;

import com.project.telegram.domain.command.BotCommandHandler;
import com.project.telegram.domain.session.ChatSession;
import com.project.telegram.utils.ChatBotUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import java.util.Locale;

@Component("badRequestHandler")
public class BadRequestHandler extends BotCommandHandler {

    private final MessageSource messageSource;

    @Autowired
    public BadRequestHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Mono<BotApiMethod<?>> handle(Message message, ChatSession session) {
        Long chatId = message.getChatId();
        Locale locale = ChatBotUtility.getLocale(message);

        String text = messageSource
                .getMessage("ILLEGAL_ARGUMENT", null, locale);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return Mono.just(sendMessage);
    }

    @Override
    public Mono<BotApiMethod<?>> handle(CallbackQuery callbackQuery, ChatSession session, Locale locale) {
        Long chatId = session.getChatId();

        String textToSend = messageSource
                .getMessage("ILLEGAL_ARGUMENT", null, locale);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        return Mono.just(sendMessage);
    }
}
