package com.animal.applicationservice.event.handler;

import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.repository.ApplicationRepository;
import com.animal.applicationservice.event.model.ApplicationApprovedEvent;
import com.animal.applicationservice.event.model.ApplicationCancelledEvent;
import com.animal.applicationservice.event.model.ApplicationCreatedEvent;
import com.animal.applicationservice.event.model.ApplicationRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ApplicationEventHandler {

    @Autowired
    private ApplicationRepository applicationRepository;

    @EventHandler
    public void handle(ApplicationCreatedEvent event) {
        Application application = Application
                .builder()
                .applicationId(event.getApplicationId())
                .animalProfileId(event.getAnimalProfileId())
                .userProfileId(event.getUserProfileId())
                .applicationStatus(event.getApplicationStatus())
                .build();

        applicationRepository.save(application).subscribe();
    }

    @EventHandler
    public void handle(ApplicationCancelledEvent event){
        applicationRepository
                .findById(event.getApplicationId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(event.getApplicationId())))
                .map(e -> {
                    e.setApplicationStatus(event.getApplicationStatus());
                    applicationRepository.save(e);
                    return e;
                })
                .subscribe();
    }

    @EventHandler
    public void handle(ApplicationApprovedEvent event){
        applicationRepository
                .findById(event.getApplicationId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(event.getApplicationId())))
                .map(e -> {
                    e.setApplicationStatus(event.getApplicationStatus());
                    applicationRepository.save(e);
                    return e;
                })
                .subscribe();
    }

    @EventHandler
    public void handle(ApplicationRejectedEvent event){
        applicationRepository
                .findById(event.getApplicationId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(event.getApplicationId())))
                .map(e -> {
                    e.setApplicationStatus(event.getApplicationStatus());
                    applicationRepository.save(e);
                    return e;
                })
                .subscribe();
    }
}
