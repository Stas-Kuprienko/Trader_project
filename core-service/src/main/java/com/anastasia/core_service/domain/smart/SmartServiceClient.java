package com.anastasia.core_service.domain.smart;

import com.anastasia.core_service.domain.event.NotificationAssistant;
import com.anastasia.core_service.entity.Account;
import com.anastasia.smart_service.AutoTradeGrpc;
import com.anastasia.smart_service.Smart;
import com.anastasia.trade_project.models.StrategyDefinition;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SmartServiceClient {

    private final AutoTradeGrpc.AutoTradeStub stub;
    private final AutoTradeGrpc.AutoTradeBlockingStub blockingStub;
    private final NotificationAssistant notificationAssistant;
    private final ExceptionHandler exceptionHandler;
    private final Map<TradeSubscription, StreamObserver<Smart.SubscribeRequest>> subscriptionRequestStore;
    private final Map<TradeSubscription, StreamObserver<Smart.SubscribeResponse>> subscriptionResponseStore;
    private final Map<TradeSubscription, StreamObserver<Smart.UnsubscribeResponse>> unsubscriptionResponseStore;


    @Autowired
    public SmartServiceClient(@Qualifier("managedChannelSmartService") ManagedChannel managedChannelSmartService,
                              NotificationAssistant notificationAssistant, ExceptionHandler exceptionHandler) {

        this.stub = AutoTradeGrpc.newStub(managedChannelSmartService);
        this.blockingStub = AutoTradeGrpc.newBlockingStub(managedChannelSmartService);
        this.notificationAssistant = notificationAssistant;
        this.exceptionHandler = exceptionHandler;
        subscriptionRequestStore = new ConcurrentHashMap<>();
        subscriptionResponseStore = new ConcurrentHashMap<>();
        unsubscriptionResponseStore = new ConcurrentHashMap<>();
    }


    public Mono<Void> subscribe(Securities securities, Account account, StrategyDefinition strategy) {
        Smart.Security securityToRequest = Smart.Security.newBuilder()
                .setTicker(securities.getTicker())
                .setBoard(securities.getBoard().name())
                .setExchange(securities.getExchange().name())
                .build();
        Smart.Account accountToRequest = Smart.Account.newBuilder()
                .setClientId(account.getClientId())
                .setBroker(account.getBroker().name())
                .setToken(account.getToken())
                .build();
        Smart.Strategy strategyToRequest = Smart.Strategy.newBuilder()
                .setName(strategy.getName())
                .setTradeScope(convertTradeScope(strategy.getTradeScope()))
                .build();
        return Mono.just(Smart.SubscribeRequest.newBuilder()
                        .setSecurity(securityToRequest)
                        .setAccount(accountToRequest)
                        .setStrategy(strategyToRequest)
                        .build())
                .doOnNext(request ->
                        subscribeRequestStreamObserver(
                                new TradeSubscription(securities, strategy.getName(), strategy.getTradeScope()))
                        .onNext(request))
                .then();
    }

    public Mono<Void> unsubscribe(Securities securities, Account account, StrategyDefinition strategy) {
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
                .setName(strategy.getName())
                .setTradeScope(convertTradeScope(strategy.getTradeScope()))
                .build();
        return Mono.just(Smart.UnsubscribeRequest.newBuilder()
                        .setSecurity(securityToRequest)
                        .setAccount(accountToRequest)
                        .setStrategy(strategyToRequest)
                        .build())
                .doOnNext(request -> stub
                        .unsubscribe(request, unsubscribeRequestStreamObserver(
                                new TradeSubscription(securities, strategy.getName(), strategy.getTradeScope()))))
                .then();
    }

    public Flux<String> strategies() {
        return Mono.just(blockingStub.getStrategies(Empty.newBuilder().build()))
                .flatMapIterable(source -> source
                        .getItemList()
                        .stream()
                        .toList());
    }

    public Mono<Void> completeSubscription(TradeSubscription subscription) {
        return Mono.just(subscriptionResponseStore.get(subscription))
                .map(responseStreamObserver -> {
                    if (responseStreamObserver != null) {
                        responseStreamObserver.onCompleted();
                        subscriptionResponseStore.remove(subscription);
                    }
                    return responseStreamObserver;
                })
                .then();
    }


    private StreamObserver<Smart.SubscribeRequest> subscribeRequestStreamObserver(TradeSubscription subscription) {
        StreamObserver<Smart.SubscribeRequest> requestStreamObserver = subscriptionRequestStore.get(subscription);
        if (requestStreamObserver == null) {
            var responseStreamObserver = new SubscribeResponseStreamObserver(notificationAssistant, exceptionHandler);
            requestStreamObserver = stub.subscribe(responseStreamObserver);
            subscriptionRequestStore.put(subscription, requestStreamObserver);
            subscriptionResponseStore.put(subscription, responseStreamObserver);
        }
        return requestStreamObserver;
    }

    private StreamObserver<Smart.UnsubscribeResponse> unsubscribeRequestStreamObserver(TradeSubscription subscription) {
        return unsubscriptionResponseStore
                .computeIfAbsent(subscription, k -> new UnsubscribeResponseStreamObserver(notificationAssistant, exceptionHandler));
    }

    private Smart.TradeScope convertTradeScope(TradeScope tradeScope) {
        return Smart.TradeScope.valueOf(tradeScope.name());
    }
}
