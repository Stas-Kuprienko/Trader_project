package com.anastasia.core_service.domain.smart;

import com.anastasia.core_service.entity.user.Account;
import com.anastasia.smart_service.AutoTradeGrpc;
import com.anastasia.smart_service.Smart;
import com.anastasia.trade_project.dto.StrategyDefinition;
import com.anastasia.trade_project.enums.TradeScope;
import com.anastasia.trade_project.markets.Securities;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SmartServiceClient {

    private final AutoTradeGrpc.AutoTradeStub stub;
    private final AutoTradeGrpc.AutoTradeBlockingStub blockingStub;
    private final StreamObserver<Smart.SubscribeResponse> subscribeResponse;
    private final StreamObserver<Smart.UnsubscribeResponse> unsubscribeResponse;


    @Autowired
    public SmartServiceClient(@Qualifier("managedChannelSmartService") ManagedChannel managedChannelSmartService,
                              NotificationHandler notificationHandler, ExceptionHandler exceptionHandler) {
        this.stub = AutoTradeGrpc.newStub(managedChannelSmartService);
        this.blockingStub = AutoTradeGrpc.newBlockingStub(managedChannelSmartService);
        this.subscribeResponse = new SubscriptionStreamObserver(notificationHandler, exceptionHandler);
        this.unsubscribeResponse = new UnsubscriptionStreamObserver(notificationHandler, exceptionHandler);
    }


    public Mono<Void> subscribe(Securities securities, Account account, String tradeStrategy, TradeScope tradeScope) {
        Smart.Security securityToRequest = Smart.Security.newBuilder()
                .setTicker(securities.getTicker())
                .setBoard(securities.getBoard().name())
                .setExchange(securities.getExchange().name())
                .build();
        Smart.Account accountToRequest = Smart.Account.newBuilder()
                .setClientId(account.getClientId())
                .setBroker(account.getBroker().title())
                .setToken(account.getToken())
                .build();
        Smart.Strategy strategyToRequest = Smart.Strategy.newBuilder()
                .setName(tradeStrategy)
                .setTradeScope(convertTradeScope(tradeScope))
                .build();
        return Mono.just(Smart.SubscribeRequest.newBuilder()
                        .setSecurity(securityToRequest)
                        .setAccount(accountToRequest)
                        .setStrategy(strategyToRequest)
                        .build())
                .doOnNext(request -> stub.subscribe(subscribeResponse).onNext(request))
                .then();
    }

    public Mono<Void> unsubscribe(Securities securities, Account account, String tradeStrategy, TradeScope tradeScope) {
        Smart.Security securityToRequest = Smart.Security.newBuilder()
                .setTicker(securities.getTicker())
                .setBoard(securities.getBoard().name())
                .setExchange(securities.getExchange().name())
                .build();
        Smart.Account accountToRequest = Smart.Account.newBuilder()
                .setClientId(account.getClientId())
                .setBroker(account.getBroker().title())
                .setToken(account.getToken())
                .build();
        Smart.Strategy strategyToRequest = Smart.Strategy.newBuilder()
                .setName(tradeStrategy)
                .setTradeScope(convertTradeScope(tradeScope))
                .build();
        return Mono.just(Smart.UnsubscribeRequest.newBuilder()
                        .setSecurity(securityToRequest)
                        .setAccount(accountToRequest)
                        .setStrategy(strategyToRequest)
                        .build())
                .doOnNext(request -> stub.unsubscribe(request, unsubscribeResponse))
                .then();
    }

    public Flux<StrategyDefinition> strategies() {
        return Mono.just(blockingStub.getStrategies(Empty.newBuilder().build()))
                .flatMapIterable(source -> source
                        .getItemList()
                        .stream()
                        .map(sd -> new StrategyDefinition(sd.getName(), sd.getDescription()))
                        .toList());
    }


    private Smart.TradeScope convertTradeScope(TradeScope tradeScope) {
        return Smart.TradeScope.valueOf(tradeScope.name());
    }
}
