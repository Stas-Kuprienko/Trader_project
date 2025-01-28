package com.project.notifications.service.impl;

import com.project.notifications.datasource.jpa.SubscriberRepository;
import com.project.notifications.entity.Subscriber;
import com.project.notifications.service.SubscriberService;
import com.project.events.NotifySubscriptionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository repository;

    @Autowired
    public SubscriberServiceImpl(SubscriberRepository repository) {
        this.repository = repository;
    }


    @Override
    public Subscriber create(NotifySubscriptionEvent subscriptionEvent) {
        return null;
    }

    @Override
    public Subscriber getById(UUID accountId) {
        return null;
    }

    @Override
    public void update(UUID accountId, NotifySubscriptionEvent subscriptionEvent) {

    }

    @Override
    public void delete(UUID accountId) {

    }
}
