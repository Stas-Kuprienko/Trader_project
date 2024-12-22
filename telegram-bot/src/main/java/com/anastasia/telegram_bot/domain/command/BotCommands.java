package com.anastasia.telegram_bot.domain.command;

public enum BotCommands {

    START("/start"),
    HELP("/help"),
    CLEAR("/clear"),
    MARKET("/market"),
    ACCOUNTS("/accounts"),
    SMART("/smart");


    public final String value;

    BotCommands(String value) {
        this.value = value;
    }
}
