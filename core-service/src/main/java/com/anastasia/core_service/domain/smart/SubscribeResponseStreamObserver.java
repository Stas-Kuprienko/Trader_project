package com.anastasia.core_service.domain.smart;

import com.anastasia.core_service.domain.event.NotificationAssistant;
import com.anastasia.smart_service.Smart;
import io.grpc.stub.StreamObserver;

public class SubscribeResponseStreamObserver implements StreamObserver<Smart.SubscribeResponse> {

    private final NotificationAssistant notificationAssistant;
    private final ExceptionHandler exceptionHandler;


    public SubscribeResponseStreamObserver(NotificationAssistant notificationAssistant, ExceptionHandler exceptionHandler) {
        this.notificationAssistant = notificationAssistant;
        this.exceptionHandler = exceptionHandler;
    }


    @Override
    public void onNext(Smart.SubscribeResponse response) {
        if (response.getPayloadCase().getNumber() == 1) {
            notificationAssistant.handle(response.getNotification());

        } else if (response.getPayloadCase().getNumber() == 2) {
            exceptionHandler.apply(response.getException());

        } else if (response.getPayloadCase().getNumber() == 3) {
            notificationAssistant.sendResponse(response.getStatus());
        }
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
