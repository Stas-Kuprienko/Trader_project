package com.anastasia.core_service.domain.smart;

import com.anastasia.core_service.domain.event_driven.MessageService;
import com.anastasia.smart_service.Smart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationHandler {

    private final MessageService messageService;


    @Autowired
    public NotificationHandler(MessageService messageService) {
        this.messageService = messageService;
    }


    public void apply(Smart.SubscribeResponse response) {

    }
    public void apply(Smart.UnsubscribeResponse response) {

    }
}
