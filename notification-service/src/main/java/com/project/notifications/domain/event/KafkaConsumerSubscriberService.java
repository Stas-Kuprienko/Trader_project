package com.project.notifications.domain.event;

import com.project.events.SubscriberDto;
import com.project.notifications.converter.SubscriberConverter;
import com.project.notifications.entity.Subscriber;
import com.project.notifications.service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumerSubscriberService {

    private final SubscriberService subscriberService;
    private final SubscriberConverter subscriberConverter;

    @Autowired
    public KafkaConsumerSubscriberService(SubscriberService subscriberService, SubscriberConverter subscriberConverter) {
        this.subscriberService = subscriberService;
        this.subscriberConverter = subscriberConverter;
    }


    @KafkaListener(topics = "subscriber-topic")
    public void handle(SubscriberDto dto) {
        log.info("Message is received: " + dto);
        switch (dto.getOperation()) {
            case SubscriberDto.Operation.SAVE -> {
                Subscriber subscriber = subscriberConverter.toEntity(dto);
                subscriberService.save(subscriber);
            }
            case SubscriberDto.Operation.DELETE -> subscriberService.delete(dto.getAccountId());
        }

    }
}
