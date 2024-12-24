package com.anastasia.core_service.service.converter;

import reactor.core.publisher.Flux;
import java.util.Collection;

public interface CollectionConverter<ENTITY, DTO> {

    Flux<DTO> toDto(Collection<ENTITY> entities);

    Flux<ENTITY> toEntity(Collection<DTO> dtoCollection);
}
