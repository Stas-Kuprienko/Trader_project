package com.project.notifications.datasource.service;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder;

    static {
        contextHolder = new ThreadLocal<>();
    }


    public static void setCurrentShard(String shardKey) {
        contextHolder.set(shardKey);
    }

    public static void clear() {
        contextHolder.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }
}
