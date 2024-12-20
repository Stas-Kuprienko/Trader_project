package com.anastasia.core_service.domain.event.impl;

import com.anastasia.core_service.domain.event.MessageService;
import com.anastasia.trade_project.notification.SubscriptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service("messageServiceSubscribeStatus")
public class MessageServiceSubscribeStatus implements MessageService<SubscriptionStatus> {

    private final String topic;
    private final KafkaTemplate<String, SubscriptionStatus> kafkaTemplate;

    @Autowired
    public MessageServiceSubscribeStatus(@Value("${spring.kafka.subscribe-status-topic}") String topic,
                                         KafkaTemplate<String, SubscriptionStatus> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate.setDefaultTopic(topic);
    }


    @Override
    public Mono<SendResult<String, SubscriptionStatus>> send(SubscriptionStatus message) {
        ProducerRecord<String, SubscriptionStatus> record = new ProducerRecord<>(topic, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }

    @Override
    public Mono<SendResult<String, SubscriptionStatus>> send(String key, SubscriptionStatus message) {
        ProducerRecord<String, SubscriptionStatus> record = new ProducerRecord<>(topic, key, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }
}
