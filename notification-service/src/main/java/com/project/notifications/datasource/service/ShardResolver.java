package com.project.notifications.datasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class ShardResolver {

    private final List<String> shardNames;

    @Autowired
    public ShardResolver(Map<Object, Object> targetDataSources) {
        this.shardNames = targetDataSources
                .keySet()
                .stream()
                .map(String.class::cast)
                .toList();
    }


    public void resolveShard(String key) {
        int shardIndex = Math.abs(key.hashCode() % shardNames.size());
        String shard = shardNames.get(shardIndex);
        DynamicDataSource.setCurrentShard(shard);
    }
}
