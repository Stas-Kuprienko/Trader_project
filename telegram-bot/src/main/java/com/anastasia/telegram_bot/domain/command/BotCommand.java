package com.anastasia.telegram_bot.domain.command;

public enum BotCommand {

    START("/start"),
    HELP("/help"),
    CLEAR("/clear"),
    MARKET("/market"),
    ACCOUNTS("/accounts");


    public final String name;

    BotCommand(String name) {
        this.name = name;
    }
}
