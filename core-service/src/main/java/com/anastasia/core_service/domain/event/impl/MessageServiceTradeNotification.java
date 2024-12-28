package com.anastasia.core_service.domain.event.impl;

import com.anastasia.core_service.domain.event.MessageService;
import com.anastasia.trade_project.events.TradeNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service("messageServiceTradeNotification")
public class MessageServiceTradeNotification implements MessageService<TradeNotification> {

    private final String topic;
    private final KafkaTemplate<String, TradeNotification> kafkaTemplate;

    @Autowired
    public MessageServiceTradeNotification(@Value("${spring.kafka.trade-notification-topic}") String topic,
                                           KafkaTemplate<String, TradeNotification> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate.setDefaultTopic(topic);
    }


    @Override
    public Mono<SendResult<String, TradeNotification>> send(TradeNotification message) {
        ProducerRecord<String, TradeNotification> record = new ProducerRecord<>(topic, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }

    @Override
    public Mono<SendResult<String, TradeNotification>> send(String key, TradeNotification message) {
        ProducerRecord<String, TradeNotification> record = new ProducerRecord<>(topic, key, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }
}
