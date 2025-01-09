package com.anastasia.core_service.service.impl;

import com.anastasia.core_service.domain.smart.SmartServiceClient;
import com.anastasia.core_service.service.AccountService;
import com.anastasia.core_service.service.SmartTradingService;
import com.anastasia.trade_project.models.StrategyDefinition;
import com.anastasia.trade_project.markets.Securities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
public class SmartTradingServiceImpl implements SmartTradingService {

    private final AccountService accountService;
    private final SmartServiceClient smartServiceClient;

    @Autowired
    public SmartTradingServiceImpl(AccountService accountService, SmartServiceClient smartServiceClient) {
        this.accountService = accountService;
        this.smartServiceClient = smartServiceClient;
    }


    @Override
    public Flux<String> strategies() {
        return smartServiceClient.strategies();
    }

    @Override
    public Mono<Void> subscribe(UUID userId, UUID accountId, Securities securities, StrategyDefinition strategyDetails) {
        return accountService
                .getById(accountId, userId)
                .flatMap(account -> smartServiceClient.subscribe(securities, account, strategyDetails));
    }

    @Override
    public Mono<Void> unsubscribe(UUID userId, UUID accountId, Securities securities, StrategyDefinition strategyDetails) {
        return accountService
                .getById(accountId, userId)
                .flatMap(account -> smartServiceClient.unsubscribe(securities, account, strategyDetails));
    }
}
