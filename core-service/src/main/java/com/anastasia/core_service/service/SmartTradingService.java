package com.anastasia.core_service.service;

import com.anastasia.trade_project.dto.StrategyDefinition;
import com.anastasia.trade_project.dto.form.SmartSubscriptionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SmartTradingService {

    Flux<StrategyDefinition> strategies();

    Mono<Void> subscribe(Long userId, SmartSubscriptionRequest request);
}
