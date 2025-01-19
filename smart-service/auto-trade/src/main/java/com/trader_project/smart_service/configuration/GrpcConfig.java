package com.trader_project.smart_service.configuration;

import com.trader_project.smart_service.util.GrpcCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PreDestroy;

@Slf4j
@Configuration
public class GrpcConfig {

    private final String finamGrpcUrl;
    private final String finamGrpcToken;
    private ManagedChannel managedChannelFinam;

    @Autowired
    public GrpcConfig(@Value("${project.resources.finam.url}") String finamGrpcUrl,
                      @Value("${project.resources.finam.token}") String finamGrpcToken) {
        this.finamGrpcUrl = finamGrpcUrl;
        this.finamGrpcToken = finamGrpcToken;
    }


    @Bean
    public GrpcCredentials finamGrpcCredentials() {
        GrpcCredentials grpcCredentials = new GrpcCredentials(finamGrpcToken);
        log.info("Call credentials gRpc for {} is registered", finamGrpcUrl);
        return grpcCredentials;
    }

    @Bean
    public ManagedChannel managedChannelFinam() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget(finamGrpcUrl).build();
        log.info("Managed channel gRpc for {} is registered", finamGrpcUrl);
        managedChannelFinam = managedChannel;
        return managedChannel;
    }


    @PreDestroy
    public void close() {
        if (managedChannelFinam != null) {
            managedChannelFinam.shutdownNow();
            log.info("Managed channel gRpc for {} is shut down", finamGrpcUrl);
        }
    }
}