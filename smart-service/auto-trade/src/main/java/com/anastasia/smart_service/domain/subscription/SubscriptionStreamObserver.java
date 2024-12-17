package com.anastasia.smart_service.domain.subscription;

import com.anastasia.smart_service.Smart;
import com.anastasia.smart_service.domain.automation.DroneLauncher;
import com.anastasia.smart_service.domain.event.EventNotificationService;
import com.anastasia.smart_service.domain.strategy.StrategyDispatcher;
import com.anastasia.smart_service.model.TradeSubscription;
import io.grpc.stub.StreamObserver;
import java.util.Map;

public class SubscriptionStreamObserver implements StreamObserver<Smart.SubscribeRequest> {

    private final StrategyDispatcher strategyDispatcher;
    private final DroneLauncher droneLauncher;
    private final EventNotificationService notificationService;
    private final StreamObserver<Smart.SubscribeResponse> responseObserver;
    private final Map<TradeSubscription, StreamObserver<Smart.SubscribeRequest>> streamObserverRequestStore;
    private TradeSubscription subscription;


    public SubscriptionStreamObserver(StrategyDispatcher strategyDispatcher,
                                      DroneLauncher droneLauncher,
                                      EventNotificationService notificationService,
                                      StreamObserver<Smart.SubscribeResponse> responseObserver,
                                      Map<TradeSubscription, StreamObserver<Smart.SubscribeRequest>> streamObserverRequestStore) {
        this.strategyDispatcher = strategyDispatcher;
        this.droneLauncher = droneLauncher;
        this.notificationService = notificationService;
        this.responseObserver = responseObserver;
        this.streamObserverRequestStore = streamObserverRequestStore;
    }


    @Override
    public void onNext(Smart.SubscribeRequest request) {
        if (subscription == null) {
            synchronized (this) {
                subscription = new TradeSubscription(request.getSecurity(), request.getStrategy());
                streamObserverRequestStore.put(subscription, this);
                notificationService.addListener(subscription, responseObserver);
            }
        }

    }

    @Override
    public void onError(Throwable throwable) {
        //TODO
        if (subscription != null) {
            synchronized (this) {
                streamObserverRequestStore.remove(subscription);
            }
        }
    }

    @Override
    public void onCompleted() {
        //TODO
        if (subscription != null) {
            synchronized (this) {
                streamObserverRequestStore.remove(subscription);
            }
        }
    }
}
