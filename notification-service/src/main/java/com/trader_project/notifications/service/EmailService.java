package com.trader_project.notifications.service;

public interface EmailService {

    void sendMessage(String text, String recipient);

    void sendHtmlEmail(String text, String recipient);
}
