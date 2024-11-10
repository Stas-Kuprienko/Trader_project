package com.anastasia.telegram_bot.configuration;

import com.anastasia.telegram_bot.domain.command.CommandHandler;
import com.anastasia.trade_project.dto.TradeNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.lang.NonNull;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@EnableKafka
public class TelegramBotConfig {

    private final String bootstrapServers;
    private final String groupId;
    private final ConfigurableApplicationContext applicationContext;

    @Autowired
    public TelegramBotConfig(@Value("${spring.kafka.consumer.bootstrap-servers}") String bootstrapServers,
                             @Value("${spring.kafka.consumer.group-id}") String groupId,
                             ConfigurableApplicationContext applicationContext) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.applicationContext = applicationContext;
    }


    // APPLICATION CONTEXT  \/

    @Bean
    public BeanPostProcessor commandComponentPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
                Class<?> beanClass = bean.getClass();
                if (beanClass.isAnnotationPresent(CommandHandler.class)) {
                    CommandHandler annotation = beanClass.getAnnotation(CommandHandler.class);
                    String command = annotation.command().name;
                    applicationContext.getBeanFactory().registerSingleton(command, bean);
                    log.info("Registering bean with command: " + command);
                }
                return bean;
            }
        };
    }
    // ******************** /\

    // REDIS ************** \/

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;
    }
    // /\ ***************** /\

    // KAFKA ************** \/

    @Bean
    public <V> ConsumerFactory<String, V> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(
                JsonDeserializer.TRUSTED_PACKAGES, TradeNotification.class.getPackage().toString());
        config.put(
                ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(
                JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
        config.put(
                JsonDeserializer.VALUE_DEFAULT_TYPE, TradeNotification.class);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public <V> ConcurrentKafkaListenerContainerFactory<String, V> kafkaListenerContainerFactory(
            ConsumerFactory<String, V> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
    // /\ ***************** /\

}
