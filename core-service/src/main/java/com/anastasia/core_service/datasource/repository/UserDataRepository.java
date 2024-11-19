package com.anastasia.core_service.datasource.repository;

import com.anastasia.core_service.entity.user.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends ReactiveCrudRepository<User, Long> {


}
