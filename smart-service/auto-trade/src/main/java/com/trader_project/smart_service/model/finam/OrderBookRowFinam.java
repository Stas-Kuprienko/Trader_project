package com.trader_project.smart_service.model.finam;

import com.trader_project.smart_service.model.OrderBookRow;
import proto.tradeapi.v1.Events;
import java.util.Objects;

public class OrderBookRowFinam implements OrderBookRow {

    private final Events.OrderBookRow orderBookRow;

    public OrderBookRowFinam(Events.OrderBookRow orderBookRow) {
        this.orderBookRow = orderBookRow;
    }

    @Override
    public double price() {
        return orderBookRow.getPrice();
    }

    @Override
    public long volume() {
        return orderBookRow.getQuantity();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderBookRowFinam that)) return false;
        return Objects.equals(orderBookRow, that.orderBookRow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderBookRow);
    }

    @Override
    public String toString() {
        return "OrderBookRowFinam{" +
                "price=" + orderBookRow.getPrice() +
                ", volume=" + orderBookRow.getQuantity() +
                '}';
    }
}
