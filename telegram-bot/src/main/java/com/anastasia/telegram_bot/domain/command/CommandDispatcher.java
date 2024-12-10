package com.anastasia.telegram_bot.domain.command;

import com.anastasia.telegram_bot.domain.session.ChatSession;
import com.anastasia.telegram_bot.domain.session.ChatSessionService;
import com.anastasia.telegram_bot.service.UserDataService;
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
    private final UserDataService userDataService;

    @Autowired
    public CommandDispatcher(ApplicationContext applicationContext,
                             ChatSessionService chatSessionService,
                             UserDataService userDataService) {
        commandHandlerStore = collectHandlers(applicationContext);
        this.chatSessionService = chatSessionService;
        this.userDataService = userDataService;
    }


    public Mono<BotApiMethodMessage> apply(Message message) {
        String command = message.getText();
        Long chatId = message.getChatId();
        chatSessionService.get(chatId)
                .switchIfEmpty(userDataService
                        .getUserByChatId(chatId)
                        .flatMap(user -> chatSessionService.create(chatId)));
        //TODO
        BotCommandHandler handler = commandHandlerStore.get(command);
        if (handler == null) {
        }
        return handler.handle(message);
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
