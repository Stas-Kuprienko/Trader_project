package com.project.notifications.configuration;

import com.project.notifications.datasource.service.ShardRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class ShardRoutingConfig {

    private final DataSource shard1DataSource;
    private final DataSource shard2DataSource;


    @Autowired
    public ShardRoutingConfig(@Qualifier("shard1DataSource") DataSource shard1DataSource,
                              @Qualifier("shard2DataSource") DataSource shard2DataSource) {

        this.shard1DataSource = shard1DataSource;
        this.shard2DataSource = shard2DataSource;
    }


    @Bean
    public DataSource routingDataSource() {
        ShardRoutingDataSource routingDataSource = new ShardRoutingDataSource();

        Map<Object, Object> dataSources = Map.of(
                "shard1DataSource", shard1DataSource,
                "shard2DataSource", shard2DataSource
        );

        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(shard1DataSource);
        return routingDataSource;
    }
}
