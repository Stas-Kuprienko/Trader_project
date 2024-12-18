package com.anastasia.core_service.domain.smart;

import com.anastasia.smart_service.Smart;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class ExceptionHandler {

    public CompletableFuture<?> apply(Throwable throwable) {
        return CompletableFuture.runAsync(() -> {
            //TODO
        });
    }

    public CompletableFuture<?> apply(Smart.Exception exception) {
        return CompletableFuture.runAsync(() -> {
            //TODO
        });
    }
}
