package com.trader_project.core_service.datasource.cache;

import com.trade_project.enums.ExchangeMarket;
import com.trade_project.market.Futures;
import com.trade_project.market.Stock;
import reactor.core.publisher.Mono;
import java.util.List;

public interface MarketDataRepository {

    Mono<Boolean> putStock(Stock stock);

    Mono<Boolean> putStockList(ExchangeMarket exchange, List<Stock> stockList);

    Mono<Boolean> putFutures(Futures futures);

    Mono<Boolean> putFuturesList(ExchangeMarket exchange, List<Futures> futuresList);

    Mono<Stock> getStock(ExchangeMarket exchange, String ticker);

    Mono<List<Stock>> getStockList(ExchangeMarket exchange);

    Mono<Futures> getFutures(ExchangeMarket exchange, String ticker);

    Mono<List<Futures>> getFuturesList(ExchangeMarket exchange);
}
