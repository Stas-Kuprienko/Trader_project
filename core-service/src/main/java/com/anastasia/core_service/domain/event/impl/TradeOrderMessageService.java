package com.anastasia.core_service.domain.event.impl;

import com.anastasia.core_service.domain.event.MessageService;
import com.anastasia.trade_project.events.TradeOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service("tradeOrderMessageService")
public class TradeOrderMessageService implements MessageService<TradeOrderEvent> {

    private final String topic;
    private final KafkaTemplate<String, TradeOrderEvent> kafkaTemplate;

    @Autowired
    public TradeOrderMessageService(@Value("${spring.kafka.trade-order-topic}") String topic,
                                    KafkaTemplate<String, TradeOrderEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate.setDefaultTopic(topic);
    }


    @Override
    public Mono<SendResult<String, TradeOrderEvent>> send(TradeOrderEvent message) {
        ProducerRecord<String, TradeOrderEvent> record = new ProducerRecord<>(topic, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }

    @Override
    public Mono<SendResult<String, TradeOrderEvent>> send(String key, TradeOrderEvent message) {
        ProducerRecord<String, TradeOrderEvent> record = new ProducerRecord<>(topic, key, message);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }
}
