package com.anastasia.ui_service.configuration;

import com.anastasia.trade_project.core_client.CoreServiceClientV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Slf4j
@EnableWebMvc
@Configuration
public class UiServiceConfig {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final String SIGN_UP_PAGE = "sign-up";
    public static final String LOGIN_PAGE = "login";
    public static final String MENU_PAGE = "menu";
    public static final String REDIRECT = "redirect:";
    public static final String FORWARD = "forward:";

    private static final String OAUTH_2_SCHEME = "oauth2Scheme";


    // MAPPING *********** \/

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    // ******************* /\

    // REDIS ************* \/

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
    // ******************* /\

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
