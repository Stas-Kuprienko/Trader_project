package com.project.smart.domain.price_stream.finam_grpc;

import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;

public record FinamOrderBookStreamObserver(FinamOrderBookEntry entry) implements StreamObserver<Events.Event> {


    @Override
    public void onNext(Events.Event event) {
        entry.setOrderBook(event);
    }

    @Override
    public void onError(Throwable t) {
        entry.deactivate();
    }

    @Override
    public void onCompleted() {
        entry.deactivate();
    }
}