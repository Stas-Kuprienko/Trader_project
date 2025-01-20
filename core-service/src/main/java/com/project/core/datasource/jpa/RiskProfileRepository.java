package com.project.core.datasource.jpa;

import com.project.core.entity.RiskProfile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RiskProfileRepository extends ReactiveCrudRepository<RiskProfile, UUID> {


}
