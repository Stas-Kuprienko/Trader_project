package com.anastasia.core_service.domain.market;

import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.enums.Sorting;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketDataProvider {

    ExchangeMarket exchange();

    Flux<Stock> stocksList(int page, int count, Sorting sorting, Sorting.Direction direction);

    Mono<Stock> getStock(String ticker);

    Flux<Futures> futuresList(int page, int count, Sorting sorting, Sorting.Direction direction);

    Mono<Futures> getFutures(String ticker);
}
