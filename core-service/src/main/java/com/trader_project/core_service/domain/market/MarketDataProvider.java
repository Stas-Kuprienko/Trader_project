package com.trader_project.core_service.domain.market;

import com.trade_project.enums.ExchangeMarket;
import com.trade_project.enums.Market;
import com.trade_project.market.Futures;
import com.trade_project.market.MarketPage;
import com.trade_project.market.Securities;
import com.trade_project.market.Stock;
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
