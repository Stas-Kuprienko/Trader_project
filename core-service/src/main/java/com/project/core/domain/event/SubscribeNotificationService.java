package com.project.core.domain.event;

import com.project.events.SubscriberDto;
import org.springframework.kafka.support.SendResult;
import reactor.core.publisher.Mono;

public interface SubscribeNotificationService {

    Mono<SendResult<String, SubscriberDto>> send(SubscriberDto message);
}
