package com.project.notifications.configuration;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class DatasourceConfig {


    @Bean(name = "shard1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard1")
    public DataSource shard1DataSource() {
        return new DriverManagerDataSource();
    }


    @Bean(name = "shard2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard2")
    public DataSource shard2DataSource() {
        return new DriverManagerDataSource();
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
    public HibernatePersistenceProvider persistenceProvider() {
        return new HibernatePersistenceProvider();
    }


    @Bean(name = "shard1EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean shard1EntityManagerFactory(@Qualifier("shard1DataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();

        bean.setDataSource(dataSource);
        bean.setPackagesToScan("com.project.notifications.entity");
        bean.setPersistenceProvider(persistenceProvider());
        bean.setPersistenceUnitName("shard1DataSource");

        return bean;
    }


    @Bean(name = "shard1TransactionManager")
    public JpaTransactionManager shard1TransactionManager(@Qualifier("shard1EntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean(name = "shard2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean shard2EntityManagerFactory(@Qualifier("shard2DataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();

        bean.setDataSource(dataSource);
        bean.setPackagesToScan("com.project.notifications.entity");
        bean.setPersistenceProvider(persistenceProvider()   );
        bean.setPersistenceUnitName("shard2DataSource");

        return bean;
    }


    @Bean(name = "shard2TransactionManager")
    public JpaTransactionManager shard2TransactionManager(@Qualifier("shard2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}
