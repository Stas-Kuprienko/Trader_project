package com.anastasia.telegram_bot.utils;

import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Locale;

public final class ChatBotUtility {

    private ChatBotUtility() {}


    public static Locale getLocale(Message message) {
        return Locale.of(message
                .getFrom()
                .getLanguageCode());
    }

    public static String getUsername(Message message) {
        return message
                .getFrom()
                .getFirstName();
    }

}
