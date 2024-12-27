package com.anastasia.core_service.service;

import com.anastasia.trade_project.dto.StrategyDefinition;
import com.anastasia.trade_project.form.SmartSubscriptionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface SmartTradingService {

    Flux<StrategyDefinition> strategies();

    Mono<Void> subscribe(UUID userId, SmartSubscriptionRequest request);
}
