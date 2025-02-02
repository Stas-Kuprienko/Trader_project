package com.project.notifications.service;

import com.project.notifications.entity.Subscriber;
import java.util.UUID;

public interface SubscriberService {

    void save(Subscriber subscriber);

    Subscriber getById(UUID accountId);

    void delete(UUID accountId);
}
