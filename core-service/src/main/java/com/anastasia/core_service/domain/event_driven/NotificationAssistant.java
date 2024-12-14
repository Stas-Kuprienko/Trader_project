package com.anastasia.core_service.domain.event_driven;

import com.anastasia.smart_service.Smart;
import com.anastasia.trade_project.notification.TradeNotification;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationAssistant {

    private final MessageService<TradeNotification> messageService;

    @Autowired
    public NotificationAssistant(MessageService<TradeNotification> messageService) {
        this.messageService = messageService;
    }


    public void notify(Smart.Notification notification) {
        //TODO list
    }
}
