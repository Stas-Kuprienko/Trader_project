package com.project.smart.domain.price_stream;

import com.project.smart.domain.strategy.TradeStrategy;
import com.project.smart.model.OrderBookRow;
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
