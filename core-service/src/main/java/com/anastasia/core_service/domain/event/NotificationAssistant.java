package com.anastasia.core_service.domain.event;

import com.anastasia.smart_service.Smart;
import com.anastasia.trade_project.notification.TradeNotification;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationAssistant {

    private final MessageService<TradeNotification> messageService;

    @Autowired
    public NotificationAssistant(MessageService<TradeNotification> messageService) {
        this.messageService = messageService;
    }


    public void handle(Smart.OrderNotification notification) {
        //TODO list
    }

    public void sendResponse(Smart.StatusResponse status) {
        //TODO
    }
}
