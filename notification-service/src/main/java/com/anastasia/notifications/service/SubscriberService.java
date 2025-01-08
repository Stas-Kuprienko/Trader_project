package com.anastasia.notifications.service;

import com.anastasia.notifications.entity.Subscriber;
import com.anastasia.trade_project.events.NotifySubscriptionEvent;
import java.util.UUID;

public interface SubscriberService {

    Subscriber create(NotifySubscriptionEvent subscriptionEvent);

    Subscriber getById(UUID accountId);

    void update(UUID accountId, NotifySubscriptionEvent subscriptionEvent);

    void delete(UUID accountId);
}
