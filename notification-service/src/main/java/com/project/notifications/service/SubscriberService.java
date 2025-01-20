package com.project.notifications.service;

import com.project.notifications.entity.Subscriber;
import com.project.events.NotifySubscriptionEvent;
import java.util.UUID;

public interface SubscriberService {

    Subscriber create(NotifySubscriptionEvent subscriptionEvent);

    Subscriber getById(UUID accountId);

    void update(UUID accountId, NotifySubscriptionEvent subscriptionEvent);

    void delete(UUID accountId);
}
