package com.trader_project.smart_service.domain.price_stream.finam_grpc;

import proto.tradeapi.v1.Events;

public class FinamOrderBookEntry {

    private Events.OrderBookEvent orderBook;
    private boolean isActive;


    Events.OrderBookEvent getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(Events.Event event) {
        if (event.getPayloadCase().getNumber() == 3) {
            orderBook = event.getOrderBook();
            isActive = true;
        } else {
            if (!event.getResponse().getSuccess()) {
                //TODO
            }
        }
    }

    public boolean isEmpty() {
        return orderBook == null ||
                orderBook.getAsksList().isEmpty() &&
                        orderBook.getBidsList().isEmpty();
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }
}
