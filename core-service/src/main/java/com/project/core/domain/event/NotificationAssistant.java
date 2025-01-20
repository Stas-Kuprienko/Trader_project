package com.project.core.domain.event;

import com.project.events.TradeSubscriptionEvent;
import com.project.smart.Smart;
import java.util.concurrent.CompletableFuture;

public interface NotificationAssistant {

    CompletableFuture<?> direct(Smart.OrderNotification notification);

    CompletableFuture<?> direct(Smart.StatusResponse status, TradeSubscriptionEvent.Option option);
}
