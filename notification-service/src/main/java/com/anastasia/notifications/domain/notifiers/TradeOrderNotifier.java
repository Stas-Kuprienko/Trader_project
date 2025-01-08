package com.anastasia.notifications.domain.notifiers;

import com.anastasia.trade_project.events.TradeOrderEvent;

@Notifier(eventType = TradeOrderEvent.class)
public class TradeOrderNotifier implements EventNotifier<TradeOrderEvent> {

    @Override
    public void apply(TradeOrderEvent tradeOrderEvent) {

    }
}
