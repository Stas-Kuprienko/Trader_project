package com.project.smart.domain.subscription;

import com.project.smart.Smart;
import io.grpc.stub.StreamObserver;

public interface SubscriptionService {

    StreamObserver<Smart.SubscribeRequest> submit(StreamObserver<Smart.SubscribeResponse> responseObserver);

    void submit(Smart.UnsubscribeRequest request, StreamObserver<Smart.UnsubscribeResponse> responseObserver);
}
