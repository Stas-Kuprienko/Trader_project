package com.project.smart.domain.subscription.impl;

import com.project.smart.Smart;
import com.project.smart.domain.automation.DroneLauncher;
import com.project.smart.domain.strategy.StrategyDispatcher;
import com.project.smart.domain.subscription.SubscriptionService;
import com.project.smart.domain.subscription.SubscriptionStreamObserver;
import com.project.smart.domain.event.EventNotificationService;
import com.project.smart.model.TradeSubscription;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SubscriptionServiceImpl implements SubscriptionService {

    private final StrategyDispatcher strategyDispatcher;
    private final DroneLauncher droneLauncher;
    private final EventNotificationService notificationService;
    private final Map<TradeSubscription, StreamObserver<Smart.SubscribeRequest>> streamObserverRequestStore;

    @Autowired
    public SubscriptionServiceImpl(StrategyDispatcher strategyDispatcher,
                                   DroneLauncher droneLauncher,
                                   EventNotificationService notificationService) {
        this.strategyDispatcher = strategyDispatcher;
        this.droneLauncher = droneLauncher;
        this.notificationService = notificationService;
        streamObserverRequestStore = new ConcurrentHashMap<>();
    }


    @Override
    public StreamObserver<Smart.SubscribeRequest> submit(StreamObserver<Smart.SubscribeResponse> responseObserver) {
        return new SubscriptionStreamObserver(
                strategyDispatcher,
                droneLauncher,
                notificationService,
                responseObserver,
                streamObserverRequestStore);
    }

    @Override
    public void submit(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {

    }
}
