package com.trader_project.core_service.domain.smart;

import com.trader_project.core_service.domain.event.NotificationAssistant;
import com.trade_project.events.TradeSubscriptionEvent;
import com.trader_project.smart_service.Smart;
import io.grpc.stub.StreamObserver;
import reactor.core.publisher.Mono;

public class SubscribeResponseStreamObserver implements StreamObserver<Smart.SubscribeResponse> {

    private final NotificationAssistant notificationAssistant;
    private final ExceptionHandler exceptionHandler;


    public SubscribeResponseStreamObserver(NotificationAssistant notificationAssistant, ExceptionHandler exceptionHandler) {
        this.notificationAssistant = notificationAssistant;
        this.exceptionHandler = exceptionHandler;
    }


    @Override
    public void onNext(Smart.SubscribeResponse response) {
        switch (response.getPayloadCase().getNumber()) {
            case 1 -> Mono
                    .fromFuture(notificationAssistant.direct(response.getNotification()))
                    .subscribe();
            case 2 -> Mono
                    .fromFuture(notificationAssistant.direct(response.getStatus(), TradeSubscriptionEvent.SUBSCRIBE))
                    .subscribe();
            case 3 -> Mono
                    .fromFuture(exceptionHandler.apply(response.getException()))
                    .subscribe();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Mono.fromFuture(exceptionHandler.apply(throwable))
                .subscribe();
    }

    @Override
    public void onCompleted() {
        //TODO implementation
    }
}
