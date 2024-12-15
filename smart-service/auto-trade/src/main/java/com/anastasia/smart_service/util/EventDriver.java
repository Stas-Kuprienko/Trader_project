package com.anastasia.smart_service.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EventDriver {

    private final ScheduledExecutorService scheduledExecutorService;
    private final Runnable runnable;
    private ScheduledFuture<?> scheduledFuture;


    public EventDriver(ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.runnable = runnable;
    }


    public void start(long delay, TimeUnit timeUnit) {
        if (scheduledFuture == null || scheduledFuture.isDone()) {
            scheduledFuture = scheduledExecutorService.schedule(runnable, delay, timeUnit);
        }
    }

    public void stop() {
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(true);
        }
    }

    public boolean isDisabled() {
        return scheduledFuture.isDone();
    }
}
