package com.anastasia.core_service.datasource.cache;

import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class MarketDataCache {

    private static final String STOCK_KEY = "_STOCK";
    private static final String FUTURES_KEY = "_FUTURES";

    private final ReactiveRedisTemplate<String, Stock> stockRedisTemplate;
    private final ReactiveRedisTemplate<String, Futures> futuresRedisTemplate;


    @Autowired
    public MarketDataCache(@Qualifier("stockRedisTemplate") ReactiveRedisTemplate<String, Stock> stockRedisTemplate,
                           @Qualifier("futuresRedisTemplate") ReactiveRedisTemplate<String, Futures> futuresRedisTemplate) {
        this.stockRedisTemplate = stockRedisTemplate;
        this.futuresRedisTemplate = futuresRedisTemplate;
    }


    @PostConstruct
    public void init() {
        for (ExchangeMarket e : ExchangeMarket.values()) {

            String stockKey = e + STOCK_KEY;
            Duration stockDuration = Duration.of(1, ChronoUnit.MINUTES);
            stockRedisTemplate.expire(stockKey, stockDuration);
            log.info("Cache duration for {} key is set to {}", stockKey, stockDuration);

            String futuresKey = e + FUTURES_KEY;
            Duration futuresDuration = Duration.of(1, ChronoUnit.MINUTES);
            futuresRedisTemplate.expire(futuresKey, futuresDuration);
            log.info("Cache duration for {} key is set to {}", futuresKey, futuresDuration);
        }
    }


    public Mono<Void> putStock(Stock stock) {
        return stockRedisTemplate
                .opsForHash()
                .put(stock.getExchange() + STOCK_KEY, stock.getTicker(), stock)
                .doOnNext(b -> log.debug("The object is cached: {}", stock))
                .then();
    }

    public Mono<Void> putStockList(List<Stock> stockList) {
        var opsForHash = stockRedisTemplate.opsForHash();
        return Flux.fromIterable(stockList)
                .map(stock -> opsForHash.put(stock.getExchange() + STOCK_KEY, stock.getTicker(), stock))
                .doOnNext(b -> log.debug("The objects is cached: {}", stockList))
                .then();
    }

    public Mono<Void> putFutures(Futures futures) {
        return futuresRedisTemplate
                .opsForHash()
                .put(futures.getExchange() + FUTURES_KEY, futures.getTicker(), futures)
                .doOnNext(b -> log.debug("The object is cached: {}", futures))
                .then();
    }

    public Mono<Void> putFuturesList(List<Futures> futuresList) {
        var opsForHash = futuresRedisTemplate.opsForHash();
        return Flux.fromIterable(futuresList)
                .map(futures -> opsForHash.put(futures.getExchange() + FUTURES_KEY, futures.getTicker(), futures))
                .doOnNext(b -> log.debug("The objects is cached: {}", futuresList))
                .then();
    }

    public Mono<Stock> getStock(ExchangeMarket exchange, String ticker) {
        return stockRedisTemplate.opsForHash()
                .get(exchange + STOCK_KEY, ticker)
                .map(Stock.class::cast)
                .doOnNext(stock -> log.debug("The object is retrieved: {}", stock));
    }

    public Mono<List<Stock>> getStockList(ExchangeMarket exchange) {
        return stockRedisTemplate.opsForHash()
                .entries(exchange + STOCK_KEY)
                .map(e -> (Stock) e.getValue())
                .collectList();
    }

    public Mono<Futures> getFutures(ExchangeMarket exchange, String ticker) {
        return futuresRedisTemplate.opsForHash()
                .get(exchange + FUTURES_KEY, ticker)
                .map(Futures.class::cast)
                .doOnNext(futures -> log.debug("The object is retrieved: {}", futures));
    }

    public Mono<List<Futures>> getFuturesList(ExchangeMarket exchange) {
        return futuresRedisTemplate.opsForHash()
                .entries(exchange + FUTURES_KEY)
                .map(e -> (Futures) e.getValue())
                .collectList();
    }
}
