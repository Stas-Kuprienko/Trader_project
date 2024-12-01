package com.anastasia.core_service.datasource.cache;

import com.anastasia.trade_project.markets.Securities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MarketDataCache {

    private final ReactiveRedisTemplate<String, Securities> redisTemplate;

    @Autowired
    public MarketDataCache(ReactiveRedisTemplate<String, Securities> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}
