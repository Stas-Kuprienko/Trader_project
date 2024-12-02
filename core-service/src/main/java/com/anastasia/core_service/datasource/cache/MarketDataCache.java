package com.anastasia.core_service.datasource.cache;

import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Securities;
import com.anastasia.trade_project.markets.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MarketDataCache(ReactiveRedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }


    @PostConstruct
    public void init() {
        for (ExchangeMarket e : ExchangeMarket.values()) {

            String stockKey = e + STOCK_KEY;
            Duration stockDuration = Duration.of(1, ChronoUnit.HOURS);
            redisTemplate.expire(stockKey, stockDuration);
            log.info("Cache duration for {} key is set to {}", stockKey, stockDuration);

            String futuresKey = e + FUTURES_KEY;
            Duration futuresDuration = Duration.of(1, ChronoUnit.HOURS);
            redisTemplate.expire(futuresKey, futuresDuration);
            log.info("Cache duration for {} key is set to {}", futuresKey, futuresDuration);
        }
    }


    public Mono<Void> putStock(Stock stock) {
        return redisTemplate
                .opsForHash()
                .put(stock.getExchange() + STOCK_KEY, stock.getTicker(), writeAsString(stock))
                .doOnNext(b -> log.info("The object is cached: {}", stock))
                .then();
    }

    public Mono<Void> putStockList(List<Stock> stockList) {
        var opsForHash = redisTemplate.opsForHash();
        return Flux.fromIterable(stockList)
                .map(stock -> opsForHash.put(stock.getExchange() + STOCK_KEY, stock.getTicker(), writeAsString(stock)))
                .doOnNext(b -> log.info("The objects is cached: {}", stockList))
                .then();
    }

    public Mono<Void> putFutures(Futures futures) {
        return redisTemplate
                .opsForHash()
                .put(futures.getExchange() + FUTURES_KEY, futures.getTicker(), writeAsString(futures))
                .doOnNext(b -> log.info("The object is cached: {}", futures))
                .then();
    }

    public Mono<Void> putFuturesList(List<Futures> futuresList) {
        var opsForHash = redisTemplate.opsForHash();
        return Flux.fromIterable(futuresList)
                .map(futures -> opsForHash.put(futures.getExchange() + FUTURES_KEY, futures.getTicker(), writeAsString(futures)))
                .doOnNext(b -> log.info("The objects is cached: {}", futuresList))
                .then();
    }

    public Mono<Stock> getStock(ExchangeMarket exchange, String ticker) {
        return redisTemplate.opsForHash()
                .get(exchange + STOCK_KEY, ticker)
                .map(value -> readObject(value, Stock.class))
                .doOnNext(stock -> log.info("The object is retrieved: {}", stock));
    }

    public Mono<List<Stock>> getStockList(ExchangeMarket exchange) {
        return redisTemplate.opsForHash()
                .entries(exchange + STOCK_KEY)
                .map(e -> readObject(e.getValue(), Stock.class))
                .collectList();
    }

    public Mono<Futures> getFutures(ExchangeMarket exchange, String ticker) {
        return redisTemplate.opsForHash()
                .get(exchange + FUTURES_KEY, ticker)
                .map(value -> readObject(value, Futures.class))
                .doOnNext(futures -> log.info("The object is retrieved: {}", futures));
    }

    public Mono<List<Futures>> getFuturesList(ExchangeMarket exchange) {
        return redisTemplate.opsForHash()
                .entries(exchange + FUTURES_KEY)
                .map(e -> readObject(e.getValue(), Futures.class))
                .collectList();
    }


    private <S extends Securities> String writeAsString(S securities) {
        try {
            return objectMapper.writeValueAsString(securities);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private <S extends Securities> S readObject(Object json, Class<S> type) {
        try {
            return objectMapper.readValue((String) json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
