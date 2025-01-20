package com.project.smart.domain.event.impl;

import com.project.smart.Smart;
import com.project.smart.domain.event.EventNotificationService;
import com.project.smart.model.TradeSubscription;
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
    public void addListener(Smart.SubscribeRequest request, StreamObserver<Smart.SubscribeResponse> responseObserver) {
        TradeSubscription subscription = new TradeSubscription(request.getSecurity(), request.getStrategy());
        listeners.put(subscription, responseObserver);
        Smart.SubscribeResponse response = Smart.SubscribeResponse
                .newBuilder()
                .setStatus(convert(request, true))
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


    private Smart.StatusResponse convert(Smart.SubscribeRequest request, boolean success) {
        return Smart.StatusResponse.newBuilder()
                .setAccount(request.getAccount())
                .setSecurity(request.getSecurity())
                .setStrategy(request.getStrategy())
                .setSuccess(success)
                .build();
    }
}
