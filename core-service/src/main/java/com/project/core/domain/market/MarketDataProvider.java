package com.project.core.domain.market;

import com.project.enums.ExchangeMarket;
import com.project.enums.Market;
import com.project.market.Futures;
import com.project.market.MarketPage;
import com.project.market.Securities;
import com.project.market.Stock;
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
