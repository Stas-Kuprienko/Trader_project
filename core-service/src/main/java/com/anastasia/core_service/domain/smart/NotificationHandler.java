package com.anastasia.core_service.domain.smart;

import com.anastasia.core_service.domain.notification.NotificationService;
import com.anastasia.smart_service.Smart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationHandler {

    private final NotificationService notificationService;


    @Autowired
    public NotificationHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    public void apply(Smart.SubscribeResponse response) {

    }
    public void apply(Smart.UnsubscribeResponse response) {

    }
}
