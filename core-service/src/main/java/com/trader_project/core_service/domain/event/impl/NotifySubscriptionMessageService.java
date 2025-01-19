package com.trader_project.core_service.domain.event.impl;

import com.trader_project.core_service.domain.event.MessageService;
import com.trade_project.events.NotifySubscriptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service("notifySubscriptionMessageService")
public class NotifySubscriptionMessageService implements MessageService<NotifySubscriptionEvent> {

    private final String topic;
    private final KafkaTemplate<String, NotifySubscriptionEvent> kafkaTemplate;

    @Autowired
    public NotifySubscriptionMessageService(@Value("${spring.kafka.notify-subscription-topic}") String topic,
                                            KafkaTemplate<String, NotifySubscriptionEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate.setDefaultTopic(topic);
    }


    @Override
    public Mono<SendResult<String, NotifySubscriptionEvent>> send(NotifySubscriptionEvent message) {
        ProducerRecord<String, NotifySubscriptionEvent> record = new ProducerRecord<>(topic, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }

    @Override
    public Mono<SendResult<String, NotifySubscriptionEvent>> send(String key, NotifySubscriptionEvent message) {
        ProducerRecord<String, NotifySubscriptionEvent> record = new ProducerRecord<>(topic, key, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }
}
