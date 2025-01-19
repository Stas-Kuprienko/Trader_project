package com.trader_project.smart_service.domain.price_stream;

import com.trader_project.smart_service.Smart;
import com.trader_project.smart_service.domain.strategy.TradeStrategy;

public interface PriceStreamProvider {

    PriceStream subscribe(Smart.Security security, TradeStrategy strategy);

    void unsubscribe(Smart.Security security, TradeStrategy strategy);
}
