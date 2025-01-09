package com.anastasia.core_service.domain.event;

import org.springframework.kafka.support.SendResult;
import reactor.core.publisher.Mono;
import java.io.Serializable;

public interface MessageService<MESSAGE extends Serializable> {

    Mono<SendResult<String, MESSAGE>> send(MESSAGE message);

    Mono<SendResult<String, MESSAGE>> send(String key, MESSAGE message);
}
