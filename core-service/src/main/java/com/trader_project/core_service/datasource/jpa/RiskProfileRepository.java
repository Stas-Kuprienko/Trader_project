package com.trader_project.core_service.datasource.jpa;

import com.trader_project.core_service.entity.RiskProfile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RiskProfileRepository extends ReactiveCrudRepository<RiskProfile, UUID> {


}
