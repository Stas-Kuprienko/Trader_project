package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.configuration.CoreServiceConfig;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Converter <ENTITY, DTO> {

    Mono<DTO> toDto(ENTITY entity);

    Mono<ENTITY> toEntity(DTO dto);

    @Nullable
    default String localDateToString(@Nullable LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.format(CoreServiceConfig.DATE_FORMAT);
        }
    }

    @Nullable
    default String localDateTimeToString(@Nullable LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            return dateTime.format(CoreServiceConfig.DATE_TIME_FORMAT);
        }
    }

    @Nullable
    default LocalDate stringToLocalDate(@Nullable String date) {
        if (date != null) {
            return LocalDate.parse(date, CoreServiceConfig.DATE_FORMAT);
        } else {
            return null;
        }
    }

    @Nullable
    default LocalDateTime stringToLocalDateTime(@Nullable String dateTime) {
        if (dateTime != null) {
            return LocalDateTime.parse(dateTime, CoreServiceConfig.DATE_TIME_FORMAT);
        } else {
            return null;
        }
    }

    default String toStringIfNotNull(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return null;
        }
    }
}
