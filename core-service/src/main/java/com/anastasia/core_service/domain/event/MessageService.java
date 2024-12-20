package com.anastasia.core_service.domain.event;

import org.springframework.kafka.support.SendResult;
import reactor.core.publisher.Mono;

public interface MessageService<MESSAGE> {

    Mono<SendResult<String, MESSAGE>> send(MESSAGE message);

    Mono<SendResult<String, MESSAGE>> send(String key, MESSAGE message);
}
