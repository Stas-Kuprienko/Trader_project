package com.anastasia.core_service.service.impl;

import com.anastasia.core_service.domain.market.MarketDataDispatcher;
import com.anastasia.core_service.domain.market.MarketDataProvider;
import com.anastasia.core_service.domain.smart.SmartServiceClient;
import com.anastasia.core_service.entity.user.Account;
import com.anastasia.core_service.service.AccountService;
import com.anastasia.core_service.service.SmartTradingService;
import com.anastasia.trade_project.dto.StrategyDefinition;
import com.anastasia.trade_project.dto.form.SmartSubscriptionRequest;
import com.anastasia.trade_project.markets.Securities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SmartTradingServiceImpl implements SmartTradingService {

    private final AccountService accountService;
    private final MarketDataDispatcher marketDataDispatcher;
    private final SmartServiceClient smartServiceClient;

    @Autowired
    public SmartTradingServiceImpl(AccountService accountService,
                                   MarketDataDispatcher marketDataDispatcher,
                                   SmartServiceClient smartServiceClient) {
        this.accountService = accountService;
        this.marketDataDispatcher = marketDataDispatcher;
        this.smartServiceClient = smartServiceClient;
    }


    @Override
    public Flux<StrategyDefinition> strategies() {
        return smartServiceClient.strategies();
    }

    @Override
    public Mono<Void> subscribe(Long userId, SmartSubscriptionRequest request) {
        MarketDataProvider marketDataProvider = marketDataDispatcher.marketDataProvider(request.getExchange());
        return Mono.zip(marketDataProvider.getSecurities(request.getTicker(), request.getMarket()),
                        accountService.getById(request.getAccountId(), userId))
                .flatMap(tuple -> {
                    Securities securities = tuple.getT1();
                    Account account = tuple.getT2();

                    return smartServiceClient
                            .subscribe(securities, account, request.getStrategy(), request.getTradeScope());
                });
    }
}
