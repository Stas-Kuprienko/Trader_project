package com.project.smart.domain.subscription;

import com.project.smart.Smart;
import com.project.smart.domain.automation.DroneLauncher;
import com.project.smart.domain.event.EventNotificationService;
import com.project.smart.domain.strategy.StrategyDispatcher;
import com.project.smart.model.TradeSubscription;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
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
        log.info("Request is accepted: " + request.toString());
        if (subscription == null) {
            synchronized (this) {
                subscription = new TradeSubscription(request.getSecurity(), request.getStrategy());
                streamObserverRequestStore.put(subscription, this);
            }
        }
        notificationService.addListener(request, responseObserver);
    }

    @Override
    public void onError(Throwable throwable) {
        //TODO
        log.error(throwable.getMessage());
        if (subscription != null) {
            synchronized (this) {
                streamObserverRequestStore.remove(subscription);
            }
        }
    }

    @Override
    public void onCompleted() {
        log.info("Stream is completed");
        //TODO
        if (subscription != null) {
            synchronized (this) {
                streamObserverRequestStore.remove(subscription);
            }
        }
    }
}
