package com.project.core.domain.smart;

import com.project.smart.Smart;
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
