package com.project.smart.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class SmartServiceConfig {


    @Bean
    public ExecutorService virtualExecutorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public ScheduledExecutorService scheduler(@Value("${project.variables.thread-pool-size}") Integer threadPoolSize) {
        return Executors.newScheduledThreadPool(threadPoolSize);
    }
}
