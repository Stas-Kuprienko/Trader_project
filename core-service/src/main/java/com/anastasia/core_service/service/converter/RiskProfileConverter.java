package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.entity.RiskProfile;
import com.anastasia.trade_project.dto.RiskProfileDto;
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
