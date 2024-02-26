package com.animal.applicationservice.saga;

import com.animal.applicationservice.command.model.CancelApplicationCommand;
import com.animal.applicationservice.command.model.ProcessPaymentCommand;
import com.animal.applicationservice.command.model.ReleaseAnimalCommand;
import com.animal.applicationservice.command.model.ReserveAnimalCommand;
import com.animal.applicationservice.data.model.ApplicationStatus;
import com.animal.applicationservice.data.model.ApplicationStatusSummary;
import com.animal.applicationservice.event.model.*;
import com.animal.applicationservice.query.model.FetchApplicationStatusSummaryQuery;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Saga
public class SubmitApplicationSaga {
    @Autowired
    private transient ReactorCommandGateway commandGateway;
    @Autowired
    private transient ReactorQueryGateway queryGateway;
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @StartSaga
    @SagaEventHandler(associationProperty="applicationId")
    public void handle(ApplicationCreatedEvent event){
        ReserveAnimalCommand reserveAnimalCommand = ReserveAnimalCommand
                .builder()
                .animalProfileId(event.getAnimalProfileId())
                .userProfileId(event.getUserProfileId())
                .build();

        commandGateway
                .send(reserveAnimalCommand)
                .switchIfEmpty(Mono.error(new IllegalArgumentException()))
                .onErrorResume(err -> {
                    CancelApplicationCommand cancelApplicationCommand = CancelApplicationCommand
                            .builder()
                            .applicationId(event.getApplicationId())
                            .message(err.getMessage())
                            .build();
                    return commandGateway.send(cancelApplicationCommand);
                })
                .subscribe();
    }

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(AnimalReservedEvent event){
        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand
                .builder()
                .applicationId(event.getApplicationId())
                .paymentId(UUID.randomUUID().toString())
                .userProfileId(event.getUserProfileId())
                .build();

        commandGateway
                .send(processPaymentCommand)
                .timeout(Duration.of(30, ChronoUnit.MINUTES))
                .onErrorResume(err -> {
                    ReleaseAnimalCommand releaseAnimalCommand = ReleaseAnimalCommand
                            .builder()
                            .applicationId(event.getApplicationId())
                            .userProfileId(event.getUserProfileId())
                            .animalProfileId(event.getAnimalProfileId())
                            .message(err.getMessage())
                            .build();
                    return commandGateway.send(releaseAnimalCommand);
                })
                .subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(PaymentProcessedEvent event) {
        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId()),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(event.getApplicationId())
                        .applicationStatus(ApplicationStatus.CREATED)
                        .build()
        );
        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId())
        );
    }

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(AnimalReleasedEvent event) {
        CancelApplicationCommand cancelApplicationCommand = CancelApplicationCommand
                .builder()
                .applicationId(event.getApplicationId())
                .message(event.getMessage())
                .build();
        commandGateway.send(cancelApplicationCommand).subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ApplicationCancelledEvent event) {
        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId()),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(event.getApplicationId())
                        .applicationStatus(ApplicationStatus.CANCELLED)
                        .message(event.getMessage())
                        .build()
        );
        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId())
        );
    }
}
