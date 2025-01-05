package com.anastasia.notifications.service;

public interface TelegramChatService {
    void sendMessage(String text, Long chatId);
}
