package com.project.core.controller.v1;

import com.project.core.domain.event.SubscribeNotificationService;
import com.project.dto.SubscriberDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Subscribe notifications")
@RestController
@RequestMapping("/api/v1/notifications")
public class SubscribeNotificationController {

    private final SubscribeNotificationService subscribeNotificationService;

    @Autowired
    public SubscribeNotificationController(SubscribeNotificationService subscribeNotificationService) {
        this.subscribeNotificationService = subscribeNotificationService;
    }


    @PostMapping
    public Mono<ResponseEntity<?>> subscribeToNotifications(@RequestBody SubscriberDto dto) {
        return subscribeNotificationService
                .send(dto)
                .map(sr -> ResponseEntity.ok().build());
    }
}
