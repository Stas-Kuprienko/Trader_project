package com.anastasia.telegram_bot.domain.command;

import com.anastasia.telegram_bot.domain.session.ChatSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CommandDispatcher {

    private final Map<String, BotCommandHandler> commandHandlerStore;
    private final ChatSessionService chatSessionService;

    @Autowired
    public CommandDispatcher(ApplicationContext applicationContext,
                             ChatSessionService chatSessionService) {
        commandHandlerStore = collectHandlers(applicationContext);
        this.chatSessionService = chatSessionService;
    }


    public Mono<BotApiMethodMessage> apply(Message message) {
        String command = message.getText();
        Long chatId = message.getChatId();
        return chatSessionService
                .get(chatId)
                .flatMap(chatSession -> {
                    BotCommandHandler handler = commandHandlerStore.get(command);
                    if (handler == null) {
                        handler = commandHandlerStore.get(chatSession.getContext().getCommand().name);
                    }
                    log.info("Command handler {} is applied", handler.getClass().getSimpleName());
                    return handler.handle(message, chatSession);
                });
    }


    private Map<String, BotCommandHandler> collectHandlers(ApplicationContext applicationContext) {
        Map<String, BotCommandHandler> commandHandlers = new HashMap<>();
        applicationContext
                .getBeansOfType(BotCommandHandler.class)
                .forEach((key, value) -> {
                    CommandHandler handlerAnnotation = value.getClass().getAnnotation(CommandHandler.class);
                    if (handlerAnnotation != null) {
                        String command = handlerAnnotation.command().name;
                        commandHandlers.put(command, value);
                        log.info("Handler for bot command '{}' is registered", command);
                    }
                });
        return commandHandlers;
    }
}
