package com.anastasia.telegram_bot.domain.command;

import com.anastasia.telegram_bot.domain.session.ChatSession;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import reactor.core.publisher.Mono;
import java.util.Locale;

public interface BotCommandHandler {

    Mono<BotApiMethod<?>> handle(Message message, ChatSession session);

    Mono<BotApiMethod<?>> handle(CallbackQuery callbackQuery, ChatSession session, Locale locale);

    default SendMessage createSendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    default SendMessage createSendMessage(long chatId, String text, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }

    default EditMessageText editMessageText(long chatId, int messageId, String text) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(text);
        return messageText;
    }

    default EditMessageText editMessageText(long chatId, int messageId, String text, InlineKeyboardMarkup keyboard) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(text);
        messageText.setReplyMarkup(keyboard);
        return messageText;
    }
}
