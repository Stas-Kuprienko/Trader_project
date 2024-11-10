package com.anastasia.telegram_bot.domain.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommandDispatcher {

    private final Map<String, BotCommandHandler> commandHandlers;

    @Autowired
    public CommandDispatcher(ApplicationContext applicationContext) {
        commandHandlers = collectHandlers(applicationContext);
    }



    private Map<String, BotCommandHandler> collectHandlers(ApplicationContext applicationContext) {
        Map<String, BotCommandHandler> commandHandlers = new HashMap<>();
        applicationContext
                .getBeansOfType(BotCommandHandler.class)
                .forEach((key, value) -> {
                    CommandHandler handlerAnnotation = value.getClass().getAnnotation(CommandHandler.class);
                    if (handlerAnnotation == null) {
                        return;
                    }
                    commandHandlers.put(handlerAnnotation.command().name, value);
                });
        return commandHandlers;
    }
}
