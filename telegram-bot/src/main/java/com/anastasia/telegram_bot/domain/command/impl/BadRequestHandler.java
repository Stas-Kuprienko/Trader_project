package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.session.ChatSession;
import com.anastasia.telegram_bot.utils.ChatBotUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import java.util.Locale;

@Component("badRequestHandler")
public class BadRequestHandler implements BotCommandHandler {

    private final MessageSource messageSource;

    @Autowired
    public BadRequestHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Mono<? extends BotApiMethodMessage> handle(Message message, ChatSession session) {
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
    public Mono<? extends BotApiMethodMessage> handle(String text, ChatSession session, Locale locale) {
        Long chatId = session.getChatId();

        String textToSend = messageSource
                .getMessage("ILLEGAL_ARGUMENT", null, locale);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        return Mono.just(sendMessage);
    }
}
