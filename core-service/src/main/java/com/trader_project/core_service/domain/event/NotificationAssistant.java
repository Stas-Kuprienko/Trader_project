package com.trader_project.core_service.domain.event;

import com.trade_project.events.TradeSubscriptionEvent;
import com.trader_project.smart_service.Smart;
import java.util.concurrent.CompletableFuture;

public interface NotificationAssistant {

    CompletableFuture<?> direct(Smart.OrderNotification notification);

    CompletableFuture<?> direct(Smart.StatusResponse status, TradeSubscriptionEvent.Option option);
}
