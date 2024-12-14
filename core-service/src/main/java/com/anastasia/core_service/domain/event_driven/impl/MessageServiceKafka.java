package com.anastasia.core_service.domain.event_driven.impl;

import com.anastasia.core_service.domain.event_driven.MessageService;
import com.anastasia.trade_project.notification.TradeNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MessageServiceKafka implements MessageService<TradeNotification> {

    private final String topic;
    private final KafkaTemplate<String, TradeNotification> kafkaTemplate;

    @Autowired
    public MessageServiceKafka(@Value("${spring.kafka.topic}") String topic,
                               KafkaTemplate<String, TradeNotification> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        kafkaTemplate.setDefaultTopic(topic);
    }


    @Override
    public Mono<SendResult<String, TradeNotification>> send(TradeNotification notification) {
        return Mono.fromFuture(kafkaTemplate.send(topic, notification))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }

    @Override
    public Mono<SendResult<String, TradeNotification>> send(String key, TradeNotification notification) {
        return Mono.fromFuture(kafkaTemplate.send(topic, key, notification))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }
}
