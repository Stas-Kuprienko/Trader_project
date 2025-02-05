package com.project.core.domain.event.impl;

import com.project.core.domain.event.SubscribeNotificationService;
import com.project.events.SubscriberDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SubscribeNotificationServiceImpl implements SubscribeNotificationService {

    private final String topic;
    private final KafkaTemplate<String, SubscriberDto> kafkaTemplate;


    @Autowired
    public SubscribeNotificationServiceImpl(@Value("${spring.kafka.subscriber-topic}") String topic,
                                            KafkaTemplate<String, SubscriberDto> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate.setDefaultTopic(topic);
    }


    @Override
    public Mono<SendResult<String, SubscriberDto>> send(SubscriberDto subscriber) {
        ProducerRecord<String, SubscriberDto> record = new ProducerRecord<>(topic, subscriber);
        return Mono.fromFuture(kafkaTemplate.send(record))
                .doOnNext(result -> log.info("Message is sent to topic '{}'. Result: {}", topic, result.toString()));
    }
}
