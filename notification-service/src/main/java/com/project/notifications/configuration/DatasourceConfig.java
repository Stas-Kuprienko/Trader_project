package com.project.notifications.configuration;

import com.project.notifications.datasource.service.DynamicDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DatasourceConfig {


    @Bean("targetDataSources")
    public Map<Object, Object> targetDataSources(@Value("${spring.datasource.shard1.url}") String url1,
                                                 @Value("${spring.datasource.shard2.url}") String url2) {

        Map<Object, Object> map = new HashMap<>();
        map.put("shard_1", createHikariDataSource(url1));
        map.put("shard_2", createHikariDataSource(url2));
        return map;
    }


    @Bean
    public DataSource shardDataSource(Map<Object, Object> targetDataSources) {
        DynamicDataSource routingDataSource = new DynamicDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(targetDataSources.get("shard_1"));

        return routingDataSource;
    }


    @Bean
    public Flyway flywayShard1(@Value("${spring.datasource.shard1.url}") String url,
                               @Value("${spring.datasource.shard1.username}") String username,
                               @Value("${spring.datasource.shard1.password}") String password) {
        var flyway = Flyway.configure()
                .dataSource(url, username, password)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        log.info("Successful migration for shard {}", url);
        return flyway;
    }


    @Bean
    public Flyway flywayShard2(@Value("${spring.datasource.shard2.url}") String url,
                               @Value("${spring.datasource.shard2.username}") String username,
                               @Value("${spring.datasource.shard2.password}") String password) {
        var flyway = Flyway.configure()
                .dataSource(url, username, password)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        log.info("Successful migration for shard {}", url);
        return flyway;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource shardDataSource, JpaProperties jpaProperties) {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null)
                .dataSource(shardDataSource)
                .packages("com.project.notifications.entity")
                .persistenceUnit("shardedPU")
                .build();
    }


    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    private DataSource createHikariDataSource(String url) {
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
}
