package com.project.smart.util;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import java.util.concurrent.Executor;

public class GrpcCredentials extends CallCredentials {

    private static final String authorization = "X-Api-Key";

    private final String apiToken;


    public GrpcCredentials(String apiToken) {
        this.apiToken = apiToken;
    }


    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(() -> apply(metadataApplier));
    }


    private void apply(MetadataApplier applier) {
        try {
            Metadata metadata = new Metadata();
            Metadata.Key<String> key = Metadata.Key.of(authorization, Metadata.ASCII_STRING_MARSHALLER);
            metadata.put(key, apiToken);
            applier.apply(metadata);
        } catch (Exception e) {
            applier.fail(Status.UNAUTHENTICATED.withCause(e));
        }
    }
}