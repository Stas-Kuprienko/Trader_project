package com.anastasia.smart_service.domain.event.impl;

import com.anastasia.smart_service.Smart;
import com.anastasia.smart_service.domain.event.EventNotificationService;
import com.anastasia.smart_service.model.TradeSubscription;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventNotificationServiceImpl implements EventNotificationService {

    private final Map<TradeSubscription, StreamObserver<Smart.SubscribeResponse>> listeners;

    @Autowired
    public EventNotificationServiceImpl() {
        listeners = new ConcurrentHashMap<>();
    }


    @Override
    public void addListener(TradeSubscription subscription, StreamObserver<Smart.SubscribeResponse> responseObserver) {
        listeners.put(subscription, responseObserver);
        Smart.SubscribeResponse response = Smart.SubscribeResponse.newBuilder()
                .setStatus(Smart.StatusResponse.newBuilder()
                        .setSuccess(true)
                        .build())
                .build();
        responseObserver.onNext(response);
    }

    @Override
    public void notify(TradeSubscription subscription, Smart.OrderNotification notification) {
        StreamObserver<Smart.SubscribeResponse> responseObserver = listeners.get(subscription);
        if (responseObserver == null) {
            //TODO
            throw new RuntimeException();
        }
        Smart.SubscribeResponse response = Smart.SubscribeResponse.newBuilder()
                .setNotification(notification)
                .build();
        responseObserver.onNext(response);
    }

    @Override
    public void removeListener(TradeSubscription subscription) {
        StreamObserver<Smart.SubscribeResponse> responseObserver = listeners.remove(subscription);
        if (responseObserver != null) {
            responseObserver.onCompleted();
        }
    }
}
