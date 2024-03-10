package com.animal.applicationservice.query.handler;

import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.model.ApplicationStatus;
import com.animal.applicationservice.data.model.ApplicationStatusSummary;
import com.animal.applicationservice.data.repository.ApplicationRepository;
import com.animal.applicationservice.query.model.FetchApplicationByIdQuery;
import com.animal.applicationservice.query.model.FetchApplicationCountByUserProfileIdQuery;
import com.animal.applicationservice.query.model.FetchApplicationStatusSummaryQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ApplicationQueryHandler {
    @Autowired
    private ApplicationRepository applicationRepository;

    @QueryHandler
    public Mono<Application> handle(FetchApplicationByIdQuery query){
        return applicationRepository
                .findById(query.getApplicationId());
    }

    @QueryHandler
    public Mono<Long> handle(FetchApplicationCountByUserProfileIdQuery query){
        return applicationRepository
                .findByUserProfileIdAndApplicationStatus(
                        query.getUserProfileId(),
                        ApplicationStatus.CREATED
                ).count();
    }

    @QueryHandler
    public Mono<ApplicationStatusSummary> handle(FetchApplicationStatusSummaryQuery query){
        return applicationRepository
                .findById(query.getApplicationId())
                .map(application -> ApplicationStatusSummary
                        .builder()
                        .applicationId(application.getApplicationId())
                        .applicationStatus(application.getApplicationStatus())
                        .build()
                );
    }

}
