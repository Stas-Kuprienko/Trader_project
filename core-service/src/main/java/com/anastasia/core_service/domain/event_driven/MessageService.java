package com.anastasia.core_service.domain.event_driven;

import org.springframework.kafka.support.SendResult;
import reactor.core.publisher.Mono;

public interface MessageService<MESSAGE> {

    Mono<SendResult<String, MESSAGE>> send(MESSAGE notification);

    Mono<SendResult<String, MESSAGE>> send(String key, MESSAGE notification);
}
