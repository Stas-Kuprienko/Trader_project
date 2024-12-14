package com.anastasia.core_service.configuration;

import com.anastasia.trade_project.markets.Futures;
import com.anastasia.trade_project.markets.Stock;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CoreServiceConfig {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    // MAPPING *********** \/

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }
    // /\ **************** /\

    // REDIS ************* \/

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean("stockRedisTemplate")
    public ReactiveRedisTemplate<String, Stock> stockRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                                                   ObjectMapper objectMapper,
                                                                   StringRedisSerializer stringRedisSerializer) {

        Jackson2JsonRedisSerializer<Stock> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Stock.class);
        RedisSerializationContext<String, Stock> redisSerializationContext = RedisSerializationContext
                .<String, Stock>newSerializationContext(stringRedisSerializer)
                .value(valueSerializer)
                .hashKey(stringRedisSerializer)
                .hashValue(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, redisSerializationContext);
    }

    @Bean("futuresRedisTemplate")
    public ReactiveRedisTemplate<String, Futures> futuresRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                                                       ObjectMapper objectMapper,
                                                                       StringRedisSerializer stringRedisSerializer) {

        Jackson2JsonRedisSerializer<Futures> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Futures.class);
        RedisSerializationContext<String, Futures> redisSerializationContext = RedisSerializationContext
                .<String, Futures>newSerializationContext(stringRedisSerializer)
                .value(valueSerializer)
                .hashKey(stringRedisSerializer)
                .hashValue(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, redisSerializationContext);
    }
    // ******************* /\

    // \/ KAFKA ********** \/

    @Bean
    public NewTopic createTopic(@Value("${spring.kafka.topic}") String topicName,
                                @Value("${spring.kafka.partitions}") Integer partitions,
                                @Value("${spring.kafka.replicas}") Integer replicas) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }

    @Bean
    public <E> ProducerFactory<String, E> producerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public <E> KafkaTemplate<String, E> kafkaTemplate(ProducerFactory<String, E> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
    // /\ **************** /\
}
