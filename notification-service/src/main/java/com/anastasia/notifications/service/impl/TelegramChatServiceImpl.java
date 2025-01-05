package com.anastasia.notifications.service.impl;

import com.anastasia.notifications.controller.TelegramBotController;
import com.anastasia.notifications.service.TelegramChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramChatServiceImpl implements TelegramChatService {

    private final TelegramBotController telegramBot;

    @Autowired
    public TelegramChatServiceImpl(TelegramBotController telegramBot) {
        this.telegramBot = telegramBot;
    }


    @Override
    public void sendMessage(String text, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
