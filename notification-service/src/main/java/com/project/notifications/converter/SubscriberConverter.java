package com.project.notifications.converter;

import com.project.dto.SubscriberDto;
import com.project.notifications.entity.Subscriber;
import org.springframework.stereotype.Service;

@Service
public class SubscriberConverter {


    public SubscriberDto toDto(Subscriber entity) {
        return SubscriberDto.builder()
                .accountId(entity.getAccountId())

                .build();
    }

    public Subscriber toEntity(SubscriberDto dto) {
        return Subscriber.builder()
                .accountId(dto.getAccountId())

                .build();
    }
}
