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
        return chatSessionService
                .get(message.getChatId())
                .flatMap(chatSession -> {
                    BotCommandHandler handler = commandHandlerStore.get(message.getText());
                    if (handler != null) {
                        chatSession.getContext().setStep(0);
                    } else {
                        BotCommand sessionContextCommand = chatSession.getContext().getCommand();
                        if (sessionContextCommand != null) {
                            handler = commandHandlerStore.get(sessionContextCommand.name);
                        } else {
                            handler = commandHandlerStore.get(null);
                        }
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
        BotCommandHandler badRequestHandler = applicationContext.getBean("badRequestHandler", BotCommandHandler.class);
        commandHandlers.put(null, badRequestHandler);
        return commandHandlers;
    }
}
