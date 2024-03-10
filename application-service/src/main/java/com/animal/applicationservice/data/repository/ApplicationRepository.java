package com.animal.applicationservice.data.repository;

import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.model.ApplicationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ApplicationRepository extends ReactiveMongoRepository<Application, String> {
    Flux<Application> findByUserProfileIdAndApplicationStatus(String userProfileId, ApplicationStatus applicationStatus);
    Flux<Application> findByApplicationIdNotNull(Pageable pageable);
}
