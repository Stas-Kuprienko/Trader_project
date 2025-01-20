package com.project.smart.domain.event;

import com.project.smart.Smart;
import com.project.smart.model.TradeSubscription;
import io.grpc.stub.StreamObserver;

public interface EventNotificationService {

    void addListener(Smart.SubscribeRequest request, StreamObserver<Smart.SubscribeResponse> responseObserver);

    void notify(TradeSubscription subscription, Smart.OrderNotification notification);

    void removeListener(TradeSubscription subscription);
}
