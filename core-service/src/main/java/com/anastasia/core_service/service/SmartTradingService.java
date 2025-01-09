package com.anastasia.core_service.service;

import com.anastasia.trade_project.models.StrategyDefinition;
import com.anastasia.trade_project.markets.Securities;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface SmartTradingService {

    Flux<String> strategies();

    Mono<Void> subscribe(UUID userId, UUID accountId, Securities securities, StrategyDefinition strategyDetails);

    Mono<Void> unsubscribe(UUID userId, UUID accountId, Securities securities, StrategyDefinition strategyDetails);
}
