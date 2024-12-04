package com.anastasia.smart_service.domain.price_stream.finam_grpc;

import com.anastasia.smart_service.domain.price_stream.PriceStream;
import com.anastasia.smart_service.domain.strategy.TradeStrategy;
import com.anastasia.smart_service.exception.PriceStreamException;
import com.anastasia.smart_service.util.EventDriver;
import com.anastasia.smart_service.model.OrderBookRow;
import com.anastasia.smart_service.model.finam.OrderBookRowFinam;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class GrpcFinamPriceStream implements PriceStream {

    private static final long DRIVER_DELAY = 1;
    private static final TimeUnit DRIVER_TIME_UNIT = TimeUnit.SECONDS;

    private final FinamOrderBookEntry orderBookEntry;
    private final EventDriver driver;
    private final Set<TradeStrategy> subscribers;


    public GrpcFinamPriceStream(FinamOrderBookEntry orderBookEntry, EventDriver driver) {
        this.orderBookEntry = orderBookEntry;
        this.driver = driver;
        subscribers = new HashSet<>();
    }


    @Override
    public OrderBookRow bid() {
        return checkThenGet(() -> new OrderBookRowFinam(orderBookEntry.getOrderBook().getBids(0)));
    }

    @Override
    public OrderBookRow ask() {
        return checkThenGet(() -> new OrderBookRowFinam(orderBookEntry.getOrderBook().getAsks(0)));
    }

    @Override
    public List<OrderBookRowFinam> bids() {
        return checkThenGetList(() -> orderBookEntry
                .getOrderBook()
                .getBidsList()
                .stream()
                .map(OrderBookRowFinam::new)
                .toList());
    }

    @Override
    public List<OrderBookRowFinam> asks() {
        return checkThenGetList(() -> orderBookEntry
                .getOrderBook()
                .getAsksList()
                .stream()
                .map(OrderBookRowFinam::new)
                .toList());
    }

    @Override
    public void subscribe(TradeStrategy strategy) {
        if (driver.isDisabled()) {
            driver.start(DRIVER_DELAY, DRIVER_TIME_UNIT);
        }
        subscribers.add(strategy);
    }

    @Override
    public void unsubscribe(TradeStrategy strategy) {
        subscribers.remove(strategy);
        if (subscribers.isEmpty()) {
            driver.stop();
        }
    }

    @Override
    public boolean isUseless() {
        return subscribers.isEmpty();
    }

    
    private OrderBookRowFinam checkThenGet(Supplier<OrderBookRowFinam> supplier) {
        if (orderBookEntry.isActive()) {
            return supplier.get();
        } else {
            throw new PriceStreamException("FinamOrderBookEntry is inactive");
        }
    }

    private List<OrderBookRowFinam> checkThenGetList(Supplier<List<OrderBookRowFinam>> supplier) {
        if (orderBookEntry.isActive()) {
            return supplier.get();
        } else {
            throw new PriceStreamException("FinamOrderBookEntry is inactive");
        }
    }
}
