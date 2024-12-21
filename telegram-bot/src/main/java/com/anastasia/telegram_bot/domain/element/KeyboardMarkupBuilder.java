package com.anastasia.telegram_bot.domain.element;

import com.anastasia.telegram_bot.utils.ChatBotUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class KeyboardMarkupBuilder {

    private final MessageSource messageSource;

    @Autowired
    public KeyboardMarkupBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @SafeVarargs
    public final InlineKeyboardMarkup inlineKeyboardMarkup(List<InlineKeyboardButton>... buttons) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = List.of(buttons);
        keyboardMarkup.setKeyboard(buttonRows);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup flatInlineKeyboardMarkup(Locale locale, List<String> buttons) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtons = inlineKeyboardButtons(locale, buttons);
        List<List<InlineKeyboardButton>> buttonRows = List.of(keyboardButtons);
        keyboardMarkup.setKeyboard(buttonRows);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup multiInlineKeyboardMarkup(Locale locale, List<List<String>> buttonLists) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = buttonLists.stream()
                .map(list -> inlineKeyboardButtons(locale, list))
                .toList();
        keyboardMarkup.setKeyboard(buttonRows);
        return keyboardMarkup;
    }

    public List<InlineKeyboardButton> inlineKeyboardButtons(Locale locale, List<String> callbackQueries) {
        return callbackQueries.stream()
                .map(callback -> {
                    String label = messageSource
                            .getMessage(callback, null, locale);
                    var button = new InlineKeyboardButton(label);
                    button.setCallbackData(callback);
                    return button;
                })
                .toList();
    }

    public List<InlineKeyboardButton> inlineKeyboardButtons(Locale locale, String... callbackQueries) {
        return Arrays.stream(callbackQueries)
                .map(callback -> {
                    String label = messageSource
                            .getMessage(callback, null, locale);
                    var button = new InlineKeyboardButton(label);
                    button.setCallbackData(callback);
                    return button;
                })
                .toList();
    }

    /**
     * Expects as argument Map of callback data (key) and button label (value).
     * @param values {@link Map} of callback query data and button label.
     * @return List of {@link InlineKeyboardButton}.
     */
    public List<InlineKeyboardButton> inlineKeyboardButtons(Map<String, String> values) {
        return values.entrySet()
                .stream()
                .map(e -> {
                    var button = new InlineKeyboardButton(e.getValue());
                    button.setCallbackData(e.getKey());
                    return button;
                })
                .toList();
    }
}
