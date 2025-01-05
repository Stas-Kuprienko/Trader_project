package com.anastasia.notifications.domain.notifiers;

import com.anastasia.trade_project.events.SubscriptionStatus;

@Notifier(eventType = SubscriptionStatus.class)
public class SubscriptionNotifier implements EventNotifier<SubscriptionStatus> {

    @Override
    public void apply(SubscriptionStatus subscriptionStatus) {

    }
}
