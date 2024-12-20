package com.anastasia.telegram_bot.utils;

import com.anastasia.telegram_bot.domain.session.ChatSession;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Locale;

public final class ChatBotUtility {

    private ChatBotUtility() {}


    public static Locale getLocale(Message message) {
        return Locale.of(message
                .getFrom()
                .getLanguageCode());
    }

    public static Locale getLocale(CallbackQuery callbackQuery) {
        return Locale.of(callbackQuery
                .getFrom()
                .getLanguageCode());
    }

    public static String getUsername(Message message) {
        return message
                .getFrom()
                .getFirstName();
    }

    public static String extractCallbackKey(String callbackQuery) {
        String[] strings = callbackQuery.split(":");
        if (strings.length != 3) {
            throw new IllegalArgumentException("Incorrect callback query value: " + callbackQuery);
        }
        return strings[2];
    }

    public static String callBackQuery(ChatSession session, String data) {
        return session.getContext().getCommand().name +
                ':' +
                session.getContext().getStep() +
                ':' +
                data;
    }
}
