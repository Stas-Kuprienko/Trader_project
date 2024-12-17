package com.anastasia.core_service.domain.smart;

import com.anastasia.core_service.domain.event.NotificationAssistant;
import com.anastasia.smart_service.Smart;
import io.grpc.stub.StreamObserver;

public class UnsubscribeResponseStreamObserver implements StreamObserver<Smart.UnsubscribeResponse> {

    private final NotificationAssistant notificationAssistant;
    private final ExceptionHandler exceptionHandler;


    public UnsubscribeResponseStreamObserver(NotificationAssistant notificationAssistant, ExceptionHandler exceptionHandler) {
        this.notificationAssistant = notificationAssistant;
        this.exceptionHandler = exceptionHandler;
    }


    @Override
    public void onNext(Smart.UnsubscribeResponse response) {
        notificationAssistant.sendResponse(response.getStatus());
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
