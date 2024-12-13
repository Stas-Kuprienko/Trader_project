package com.anastasia.core_service.datasource.jpa;

import com.anastasia.core_service.entity.user.RiskProfile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RiskProfileRepository extends ReactiveCrudRepository<RiskProfile, UUID> {


}
