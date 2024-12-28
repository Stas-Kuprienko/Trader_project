package com.anastasia.core_service.domain.event;

import com.anastasia.smart_service.Smart;
import com.anastasia.trade_project.events.SubscriptionStatus;
import java.util.concurrent.CompletableFuture;

public interface NotificationAssistant {

    CompletableFuture<?> direct(Smart.OrderNotification notification);

    CompletableFuture<?> direct(Smart.StatusResponse status, SubscriptionStatus.Option option);
}
