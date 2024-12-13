package com.anastasia.telegram_bot.datasource.redis;

import com.anastasia.telegram_bot.datasource.MarketDataRepository;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Repository
public class MarketDataRepositoryRedis implements MarketDataRepository {

    private static final String STOCK_KEY = "_STOCK";
    private static final String FUTURES_KEY = "_FUTURES";

    private final ReactiveRedisTemplate<String, Stock> stockRedisTemplate;
    private final ReactiveRedisTemplate<String, Futures> futuresRedisTemplate;


    @Autowired
    public MarketDataRepositoryRedis(@Qualifier("stockRedisTemplate") ReactiveRedisTemplate<String, Stock> stockRedisTemplate,
                                     @Qualifier("futuresRedisTemplate") ReactiveRedisTemplate<String, Futures> futuresRedisTemplate) {
        this.stockRedisTemplate = stockRedisTemplate;
        this.futuresRedisTemplate = futuresRedisTemplate;
    }


    @Override
    public Mono<Stock> getStock(ExchangeMarket exchange, String ticker) {
        return stockRedisTemplate.opsForHash()
                .get(exchange + STOCK_KEY, ticker)
                .map(Stock.class::cast)
                .doOnNext(stock -> log.debug("The object is retrieved: {}", stock));
    }

    @Override
    public Mono<List<Stock>> getStockList(ExchangeMarket exchange) {
        return stockRedisTemplate.opsForHash()
                .entries(exchange + STOCK_KEY)
                .map(e -> (Stock) e.getValue())
                .collectList();
    }

    @Override
    public Mono<Futures> getFutures(ExchangeMarket exchange, String ticker) {
        return futuresRedisTemplate.opsForHash()
                .get(exchange + FUTURES_KEY, ticker)
                .map(Futures.class::cast)
                .doOnNext(futures -> log.debug("The object is retrieved: {}", futures));
    }

    @Override
    public Mono<List<Futures>> getFuturesList(ExchangeMarket exchange) {
        return futuresRedisTemplate.opsForHash()
                .entries(exchange + FUTURES_KEY)
                .map(e -> (Futures) e.getValue())
                .collectList();
    }
}
