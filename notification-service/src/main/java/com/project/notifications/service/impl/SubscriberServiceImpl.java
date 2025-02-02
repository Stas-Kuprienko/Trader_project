package com.project.notifications.service.impl;

import com.project.exception.NotFoundException;
import com.project.notifications.datasource.jpa.SubscriberRepository;
import com.project.notifications.datasource.service.ShardResolver;
import com.project.notifications.entity.Subscriber;
import com.project.notifications.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final ShardResolver shardResolver;
    private final SubscriberRepository repository;


    @Autowired
    public SubscriberServiceImpl(ShardResolver shardResolver,
                                 SubscriberRepository repository) {
        this.shardResolver = shardResolver;
        this.repository = repository;
    }


    @Override
    public void save(Subscriber subscriber) {
        shardResolver.resolveShard(subscriber.getAccountId().hashCode());
        repository.save(subscriber);
        shardResolver.resetShard();
    }

    @Override
    public Subscriber getById(UUID accountId) {
        shardResolver.resolveShard(accountId.hashCode());
        Optional<Subscriber> subscriber = repository.findById(accountId);
        shardResolver.resetShard();
        return subscriber.orElseThrow(() -> NotFoundException.byID(Subscriber.class, accountId));
    }

    @Override
    public void delete(UUID accountId) {

    }
}
