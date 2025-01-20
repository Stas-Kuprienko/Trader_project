package com.project.core.configuration;

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

    private final String smartServiceUrl;
    private ManagedChannel managedChannelSmartService;

    @Autowired
    public GrpcConfig(@Value("${project.variables.smart-service.url}") String smartServiceUrl) {
        this.smartServiceUrl = smartServiceUrl;
    }


    @Bean
    public ManagedChannel managedChannelSmartService() {
        //TODO temporary .usePlaintext()
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forTarget(smartServiceUrl)
                .usePlaintext()
                .build();
        log.info("Managed channel gRpc for {} is registered", smartServiceUrl);
        managedChannelSmartService = managedChannel;
        return managedChannel;
    }


    @PreDestroy
    public void close() {
        if (managedChannelSmartService != null) {
            managedChannelSmartService.shutdownNow();
            log.info("Managed channel gRpc for {} is shut down", smartServiceUrl);
        }
    }
}