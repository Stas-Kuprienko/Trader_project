package com.project.notifications.datasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import javax.activation.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ShardResolver {

    private final List<String> shardNames;

    @Autowired
    public ShardResolver(ConfigurableApplicationContext applicationContext) {
        this.shardNames = new ArrayList<>(applicationContext.getBeansOfType(DataSource.class).keySet());
    }


    public String resolveShard(String key) {
        int shardIndex = Math.abs(key.hashCode() % shardNames.size());
        return shardNames.get(shardIndex);
    }
}
