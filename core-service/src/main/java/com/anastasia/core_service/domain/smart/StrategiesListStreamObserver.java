package com.anastasia.core_service.domain.smart;

import com.anastasia.smart_service.Smart;
import io.grpc.stub.StreamObserver;
import java.util.List;

public record StrategiesListStreamObserver(List<String> strategies) implements StreamObserver<Smart.StrategiesList> {


    @Override
    public void onNext(Smart.StrategiesList strategiesList) {
        //TODO
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
