package com.project.core.service.converter;

import com.project.core.entity.RiskProfile;
import com.project.dto.RiskProfileDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RiskProfileConverter implements Converter<RiskProfile, RiskProfileDto> {

    @Override
    public Mono<RiskProfileDto> toDto(RiskProfile riskProfile) {
        //TODO
        return null;
    }

    @Override
    public Mono<RiskProfile> toEntity(RiskProfileDto riskProfileDto) {
        //TODO
        return null;
    }
}
