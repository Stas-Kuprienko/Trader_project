package com.trader_project.notifications.service;

public interface TelegramService {

    void sendMessage(String text, Long chatId);
}
