package com.anastasia.smart_service.controller;

import com.anastasia.trade_project.Smart;
import com.anastasia.trade_project.SmartServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class SmartServiceController extends SmartServiceGrpc.SmartServiceImplBase {


    @Override
    public StreamObserver<Smart.SubscribeRequest> subscribe(StreamObserver<Smart.SubscribeResponse> responseObserver) {

        return super.subscribe(responseObserver);
    }

    @Override
    public void unsubscribe(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver) {
        super.unsubscribe(request, responseObserver);
    }

    @Override
    public void getStrategies(Empty request, StreamObserver<Smart.StrategiesList> responseObserver) {
        super.getStrategies(request, responseObserver);
    }
}
