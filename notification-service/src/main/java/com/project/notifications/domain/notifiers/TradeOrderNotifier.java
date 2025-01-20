package com.project.notifications.domain.notifiers;

import com.project.events.TradeOrderEvent;

@Notifier(eventType = TradeOrderEvent.class)
public class TradeOrderNotifier implements EventNotifier<TradeOrderEvent> {

    @Override
    public void apply(TradeOrderEvent tradeOrderEvent) {

    }
}
