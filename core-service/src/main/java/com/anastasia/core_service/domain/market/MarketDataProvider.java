package com.anastasia.core_service.domain.market;

import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.enums.Market;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.MarketPage;
import com.anastasia.trade_project.markets.Securities;
import com.anastasia.trade_project.markets.Stock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketDataProvider {

    ExchangeMarket exchange();

    Flux<Stock> stocksList(MarketPage page);

    Mono<Stock> getStock(String ticker);

    Flux<Futures> futuresList(MarketPage page);

    Mono<Futures> getFutures(String ticker);

    Mono<? extends Securities> getSecurities(String ticker, Market market);
}
