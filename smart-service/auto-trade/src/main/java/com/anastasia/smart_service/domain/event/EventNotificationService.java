package com.anastasia.smart_service.domain.event;

import com.anastasia.smart_service.Smart;
import com.anastasia.smart_service.model.TradeSubscription;
import io.grpc.stub.StreamObserver;

public interface EventNotificationService {

    void addListener(TradeSubscription subscription, StreamObserver<Smart.SubscribeResponse> responseObserver);

    void notify(TradeSubscription subscription, Smart.OrderNotification notification);

    void removeListener(TradeSubscription subscription);
}
