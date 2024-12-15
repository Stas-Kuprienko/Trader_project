package com.anastasia.smart_service.domain.price_stream;

import com.anastasia.smart_service.domain.strategy.TradeStrategy;
import com.anastasia.smart_service.Smart;

public interface PriceStreamProvider {

    PriceStream subscribe(Smart.Security security, TradeStrategy strategy);

    void unsubscribe(Smart.Security security, TradeStrategy strategy);
}
