package com.anastasia.core_service.storage;

import com.anastasia.trade_project.markets.Securities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheManager {

    private final ReactiveRedisTemplate<String, Securities> redisTemplate;

    @Autowired
    public CacheManager(ReactiveRedisTemplate<String, Securities> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}
