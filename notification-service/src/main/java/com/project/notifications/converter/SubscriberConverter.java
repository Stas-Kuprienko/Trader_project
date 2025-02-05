package com.project.notifications.converter;

import com.project.events.SubscriberDto;
import com.project.notifications.entity.Subscriber;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SubscriberConverter {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public SubscriberDto toDto(Subscriber entity) {
        return SubscriberDto.builder()
                .accountId(entity.getAccountId())
                .email(entity.getEmail())
                .emailNotify(entity.isEmailNotify())
                .telegramId(entity.getTelegramId())
                .language(entity.getLanguage())
                .name(entity.getName())
                .createdAt(localDateToString(entity.getCreatedAt()))
                .updatedAt(localDateTimeToString(entity.getUpdatedAt()))
                .build();
    }

    public Subscriber toEntity(SubscriberDto dto) {
        return Subscriber.builder()
                .accountId(dto.getAccountId())
                .email(dto.getEmail())
                .emailNotify(dto.isEmailNotify())
                .telegramId(dto.getTelegramId())
                .telegramNotify(dto.isTelegramNotify())
                .language(dto.getLanguage())
                .name(dto.getName())
                .createdAt(stringToLocalDate(dto.getCreatedAt()))
                .updatedAt(stringToLocalDateTime(dto.getUpdatedAt()))
                .build();
    }


    @Nullable
    String localDateToString(@Nullable LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.format(DATE_FORMAT);
        }
    }

    @Nullable
    String localDateTimeToString(@Nullable LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            return dateTime.format(DATE_TIME_FORMAT);
        }
    }

    @Nullable
    LocalDate stringToLocalDate(@Nullable String date) {
        if (date != null) {
            return LocalDate.parse(date, DATE_FORMAT);
        } else {
            return null;
        }
    }

    @Nullable
    LocalDateTime stringToLocalDateTime(@Nullable String dateTime) {
        if (dateTime != null) {
            return LocalDateTime.parse(dateTime, DATE_TIME_FORMAT);
        } else {
            return null;
        }
    }
}
