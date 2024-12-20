package com.anastasia.telegram_bot.domain.command;

public enum BotCommands {

    START("/start"),
    HELP("/help"),
    CLEAR("/clear"),
    MARKET("/market"),
    ACCOUNTS("/accounts");


    public final String name;

    BotCommands(String name) {
        this.name = name;
    }
}
