package com.trader_project.core_service.configuration;

import com.trade_project.market.Futures;
import com.trade_project.market.Stock;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
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
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String OAUTH_2_SCHEME = "oauth2Scheme";

    private Keycloak keycloak;


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
    public NewTopic createNotifySubscriptionTopic(@Value("${spring.kafka.notify-subscription-topic}") String topicName,
                                                  @Value("${spring.kafka.partitions}") Integer partitions,
                                                  @Value("${spring.kafka.replicas}") Integer replicas) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }

    @Bean
    public NewTopic createTradeSubscriptionTopic(@Value("${spring.kafka.trade-subscription-topic}") String topicName,
                                                 @Value("${spring.kafka.partitions}") Integer partitions,
                                                 @Value("${spring.kafka.replicas}") Integer replicas) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }

    @Bean
    public NewTopic createTradeOrderTopic(@Value("${spring.kafka.trade-order-topic}") String topicName,
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

    // KEYCLOAK ********** \/

    @Bean
    public Keycloak keycloak(@Value("${project.variables.keycloak.url}") String url,
                             @Value("${project.variables.keycloak.username}") String username,
                             @Value("${project.variables.keycloak.password}") String password) {
        return this.keycloak = KeycloakBuilder.builder()
                .serverUrl(url)
                .clientId("admin-cli")
                .realm("master")
                .username(username)
                .password(password)
                .build();
    }

    @Bean
    public RealmResource realmResource(Keycloak keycloak, @Value("${project.variables.keycloak.realm}") String realm) {
        return keycloak.realm(realm);
    }
    // ******************* /\

    // CONTEXT *********** \/

    @PreDestroy
    public void close() {
        if (keycloak != null) {
            keycloak.close();
        }
    }
    // ******************* /\

    // OPEN-API ********** \/

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.info.title}") String title,
                           @Value("${springdoc.info.description}") String description,
                           @Value("${springdoc.info.version}") String version,
                           @Value("${project.variables.keycloak.url}") String authServerUrl,
                           @Value("${project.variables.keycloak.realm}") String realm) {

        Info info = new Info()
                .title(title)
                .description(description)
                .version(version);

        SecurityScheme oauth2Scheme = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/auth")
                                .tokenUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                                .scopes(new Scopes()
                                        .addString("openid", "OpenID Connect scope")
                                        .addString("profile", "Access to user profile"))));

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(OAUTH_2_SCHEME);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(new Components()
                        .addSecuritySchemes(OAUTH_2_SCHEME, oauth2Scheme));
    }
    // /\ **************** /\
}
