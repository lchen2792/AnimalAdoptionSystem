package com.animal.applicationservice.command.aggregate;

import com.animal.applicationservice.command.model.*;
import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.model.ApplicationStatus;
import com.animal.applicationservice.event.model.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Aggregate
public class ApplicationAggregate {
    @AggregateIdentifier
    private String applicationId;
    private String userProfileId;
    private String animalProfileId;
    private ApplicationStatus applicationStatus;
    @Autowired
    private Sinks.Many<ServerSentEvent<String>> reviewNotificationSink;

    @CommandHandler
    public ApplicationAggregate(CreateApplicationCommand command){
        ApplicationCreatedEvent event = ApplicationCreatedEvent
                .builder()
                .applicationId(command.getApplicationId())
                .animalProfileId(command.getAnimalProfileId())
                .userProfileId(command.getUserProfileId())
                .applicationStatus(ApplicationStatus.CREATED)
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void handle(ApplicationCreatedEvent event){
        this.applicationId = event.getApplicationId();
        this.animalProfileId = event.getAnimalProfileId();
        this.userProfileId = event.getUserProfileId();
        this.applicationStatus = event.getApplicationStatus();
    }

    @CommandHandler
    public void handle(CancelApplicationCommand command){
        ApplicationCancelledEvent event = ApplicationCancelledEvent
                .builder()
                .applicationId(command.getApplicationId())
                .applicationStatus(ApplicationStatus.CANCELLED)
                .message(command.getMessage())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void handle(ApplicationCancelledEvent event){
        this.applicationStatus = event.getApplicationStatus();
    }

    @CommandHandler
    public void handle(ApproveApplicationCommand command){
        ApplicationApprovedEvent event = ApplicationApprovedEvent
                .builder()
                .applicationId(command.getApplicationId())
                .applicationStatus(ApplicationStatus.APPROVED)
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void handle(ApplicationApprovedEvent event){
        this.applicationStatus = event.getApplicationStatus();
    }

    @CommandHandler
    public void handle(RejectApplicationCommand command){
        ApplicationRejectedEvent event = ApplicationRejectedEvent
                .builder()
                .applicationId(command.getApplicationId())
                .applicationStatus(ApplicationStatus.REJECTED)
                .message(command.getMessage())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void handle(ApplicationRejectedEvent event){
        this.applicationStatus = event.getApplicationStatus();
    }

    @CommandHandler
    public void handle(ReviewApplicationCommand command) {
        reviewNotificationSink.tryEmitNext(ServerSentEvent
                .<String>builder()
                .id(UUID.randomUUID().toString())
                .event("review application notification")
                .data(String.format(command.getApplicationId()))
                .build()
        );
    }

    @CommandHandler
    public void handle(UndoReviewCommand command) {
        ReviewUndoneEvent event = ReviewUndoneEvent
                .builder()
                .applicationId(command.getApplicationId())
                .message(command.getMessage())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void handle(ReviewUndoneEvent event) {
        this.applicationStatus = event.getApplicationStatus();
    }}
