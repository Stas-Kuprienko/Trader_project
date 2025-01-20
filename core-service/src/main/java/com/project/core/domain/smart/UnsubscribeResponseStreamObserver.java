package com.project.core.domain.smart;

import com.project.core.domain.event.NotificationAssistant;
import com.project.events.TradeSubscriptionEvent;
import com.project.smart.Smart;
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
        notificationAssistant.direct(response.getStatus(), TradeSubscriptionEvent.UNSUBSCRIBE);
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
