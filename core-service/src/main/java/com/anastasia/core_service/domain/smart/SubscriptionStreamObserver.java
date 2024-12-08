package com.anastasia.core_service.domain.smart;

import com.anastasia.smart_service.Smart;
import io.grpc.stub.StreamObserver;

public class SubscriptionStreamObserver implements StreamObserver<Smart.SubscribeResponse> {

    private final NotificationHandler notificationHandler;
    private final ExceptionHandler exceptionHandler;


    public SubscriptionStreamObserver(NotificationHandler notificationHandler, ExceptionHandler exceptionHandler) {
        this.notificationHandler = notificationHandler;
        this.exceptionHandler = exceptionHandler;
    }


    @Override
    public void onNext(Smart.SubscribeResponse response) {
        notificationHandler.apply(response);
    }

    @Override
    public void onError(Throwable throwable) {
        exceptionHandler.apply(throwable);
    }

    @Override
    public void onCompleted() {
        //TODO implementation
    }
}
