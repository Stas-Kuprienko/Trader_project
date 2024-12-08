package com.anastasia.core_service.domain.notification;

import com.anastasia.trade_project.notification.TradeNotification;

public interface NotificationService {

    void send(TradeNotification notification);

    void send(String key, TradeNotification notification);
}
