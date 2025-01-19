package com.trader_project.smart_service.domain.price_stream;

import com.trader_project.smart_service.domain.strategy.TradeStrategy;
import com.trader_project.smart_service.model.OrderBookRow;
import java.util.List;

public interface PriceStream {

    OrderBookRow bid();

    OrderBookRow ask();

    List<? extends OrderBookRow> bids();

    List<? extends OrderBookRow> asks();

    void subscribe(TradeStrategy strategy);

    void unsubscribe(TradeStrategy strategy);
    
    boolean isUseless();
}
