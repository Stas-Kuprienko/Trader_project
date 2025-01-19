package com.trader_project.telegram_bot.configuration;

import com.trader_project.telegram_bot.domain.session.ChatSession;
import com.trade_project.core_client.CoreServiceClientV1;
import com.trade_project.events.TradeOrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class TelegramBotConfig {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String bootstrapServers;
    private final String groupId;


    @Autowired
    public TelegramBotConfig(@Value("${spring.kafka.consumer.bootstrap-servers}") String bootstrapServers,
                             @Value("${spring.kafka.consumer.group-id}") String groupId) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
    }


    // MAPPING ************* \/

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    // /\ ****************** /\

    // REDIS *************** \/

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean("chatSessionRedisTemplate")
    public ReactiveRedisTemplate<String, ChatSession> chatSessionRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                                                           ObjectMapper objectMapper,
                                                                           StringRedisSerializer stringRedisSerializer) {

        Jackson2JsonRedisSerializer<ChatSession> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, ChatSession.class);
        RedisSerializationContext<String, ChatSession> redisSerializationContext = RedisSerializationContext
                .<String, ChatSession>newSerializationContext(stringRedisSerializer)
                .value(valueSerializer)
                .hashKey(stringRedisSerializer)
                .hashValue(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, redisSerializationContext);
    }
    // ******************** /\

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
                JsonDeserializer.TRUSTED_PACKAGES, TradeOrderEvent.class.getPackage().toString());
        config.put(
                ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(
                JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
        config.put(
                JsonDeserializer.VALUE_DEFAULT_TYPE, TradeOrderEvent.class);

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

    // MESSAGES *********** \/

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }
    // /\ ***************** /\

    // PROJECT ************ /\

    @Bean
    public CoreServiceClientV1 coreServiceClient(@Value("${project.variables.core-service.url}") String url) {
        CoreServiceClientV1 client = new CoreServiceClientV1(url);
        log.info("Core-service client with url {} is registered", url);
        return client;
    }
    // /\ ***************** /\
}
