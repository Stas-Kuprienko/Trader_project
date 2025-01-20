package com.project.smart.domain.price_stream;

import com.project.smart.Smart;
import com.project.smart.domain.strategy.TradeStrategy;

public interface PriceStreamProvider {

    PriceStream subscribe(Smart.Security security, TradeStrategy strategy);

    void unsubscribe(Smart.Security security, TradeStrategy strategy);
}
