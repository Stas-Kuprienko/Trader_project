package com.trader_project.smart_service.controller;

import com.google.protobuf.Empty;
import com.trader_project.smart_service.AutoTradeGrpc;
import com.trader_project.smart_service.Smart;
import com.trader_project.smart_service.domain.strategy.StrategyDispatcher;
import com.trader_project.smart_service.domain.subscription.SubscriptionService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@GrpcService
public class AutoTradeController extends AutoTradeGrpc.AutoTradeImplBase {

    private final SubscriptionService subscriptionService;
    private final StrategyDispatcher strategyDispatcher;

    @Autowired
    public AutoTradeController(SubscriptionService subscriptionService, StrategyDispatcher strategyDispatcher) {
        this.subscriptionService = subscriptionService;
        this.strategyDispatcher = strategyDispatcher;
    }


    @Override
    public StreamObserver<Smart.SubscribeRequest> subscribe(StreamObserver<Smart.SubscribeResponse> responseObserver) {
        return subscriptionService.submit(responseObserver);
    }

    @Override
    public void unsubscribe(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {
        subscriptionService.submit(request, responseObserver);
    }

    @Override
    public void getStrategies(Empty request, StreamObserver<Smart.StrategiesList> responseObserver) {
        List<String> list = strategyDispatcher.strategyList();
        Smart.StrategiesList strategiesList = Smart.StrategiesList.newBuilder()
                .addAllItem(list)
                .build();
        responseObserver.onNext(strategiesList);
        responseObserver.onCompleted();
    }
}
