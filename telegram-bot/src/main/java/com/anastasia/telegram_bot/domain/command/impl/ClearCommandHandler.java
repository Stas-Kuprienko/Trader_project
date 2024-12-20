package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.domain.command.BotCommands;
import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.command.CommandHandler;
import com.anastasia.telegram_bot.domain.session.ChatSession;
import com.anastasia.telegram_bot.domain.session.ChatSessionService;
import com.anastasia.telegram_bot.utils.ChatBotUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import java.util.Locale;

@CommandHandler(command = BotCommands.CLEAR)
public class ClearCommandHandler implements BotCommandHandler {

    private static final String MESSAGE_KEY = "CLEAR";

    private final MessageSource messageSource;
    private final ChatSessionService chatSessionService;


    @Autowired
    public ClearCommandHandler(MessageSource messageSource, ChatSessionService chatSessionService) {
        this.messageSource = messageSource;
        this.chatSessionService = chatSessionService;
    }


    @Override
    public Mono<? extends BotApiMethodMessage> handle(Message message, ChatSession session) {
        Locale locale = ChatBotUtility.getLocale(message);
        String text = messageSource.getMessage(MESSAGE_KEY, null, locale);
        return Mono.just(session)
                .doOnNext(ChatSession::clear)
                .map(b -> {
                    chatSessionService.save(session).subscribe();
                    return createSendMessage(session.getChatId(), text);
                });
    }

    @Override
    public Mono<? extends BotApiMethodMessage> handle(String text, ChatSession session, Locale locale) {
        String textToSend = messageSource.getMessage(MESSAGE_KEY, null, locale);
        return Mono.just(session)
                .doOnNext(ChatSession::clear)
                .map(b -> {
                    chatSessionService.save(session).subscribe();
                    return createSendMessage(session.getChatId(), textToSend);
                });
    }
}
