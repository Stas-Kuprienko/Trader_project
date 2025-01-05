package com.anastasia.notifications.domain.notifiers;

import com.anastasia.trade_project.events.TradeNotification;

@Notifier(eventType = TradeNotification.class)
public class TradeOrderNotifier implements EventNotifier<TradeNotification> {

    @Override
    public void apply(TradeNotification tradeNotification) {

    }
}
