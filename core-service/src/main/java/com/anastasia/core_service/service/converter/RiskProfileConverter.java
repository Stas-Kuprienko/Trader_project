package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.entity.user.RiskProfile;
import com.anastasia.trade_project.dto.RiskProfileDto;
import reactor.core.publisher.Mono;

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
