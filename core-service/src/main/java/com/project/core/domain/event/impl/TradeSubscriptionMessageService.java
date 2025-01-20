package com.project.core.domain.event.impl;

import com.project.core.domain.event.MessageService;
import com.project.events.TradeSubscriptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service("tradeSubscriptionMessageService")
public class TradeSubscriptionMessageService implements MessageService<TradeSubscriptionEvent> {

    private final String topic;
    private final KafkaTemplate<String, TradeSubscriptionEvent> kafkaTemplate;

    @Autowired
    public TradeSubscriptionMessageService(@Value("${spring.kafka.trade-subscription-topic}") String topic,
                                           KafkaTemplate<String, TradeSubscriptionEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate.setDefaultTopic(topic);
    }


    @Override
    public Mono<SendResult<String, TradeSubscriptionEvent>> send(TradeSubscriptionEvent message) {
        ProducerRecord<String, TradeSubscriptionEvent> record = new ProducerRecord<>(topic, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }

    @Override
    public Mono<SendResult<String, TradeSubscriptionEvent>> send(String key, TradeSubscriptionEvent message) {
        ProducerRecord<String, TradeSubscriptionEvent> record = new ProducerRecord<>(topic, key, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }
}
