package com.anastasia.core_service.domain.smart;

import com.anastasia.smart_service.Smart;
import io.grpc.stub.StreamObserver;

public class UnsubscriptionStreamObserver implements StreamObserver<Smart.UnsubscribeResponse> {

    private final NotificationHandler notificationHandler;
    private final ExceptionHandler exceptionHandler;


    public UnsubscriptionStreamObserver(NotificationHandler notificationHandler, ExceptionHandler exceptionHandler) {
        this.notificationHandler = notificationHandler;
        this.exceptionHandler = exceptionHandler;
    }


    @Override
    public void onNext(Smart.UnsubscribeResponse response) {
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
