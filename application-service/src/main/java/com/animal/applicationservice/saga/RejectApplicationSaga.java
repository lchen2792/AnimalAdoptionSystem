package com.animal.applicationservice.saga;

import com.animal.applicationservice.command.model.RequestReviewCommand;
import com.animal.applicationservice.command.model.UndoReviewCommand;
import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.model.ApplicationStatus;
import com.animal.applicationservice.data.model.ApplicationStatusSummary;
import com.animal.applicationservice.event.model.*;
import com.animal.applicationservice.query.model.FetchApplicationByIdQuery;
import com.animal.applicationservice.query.model.FetchApplicationStatusSummaryQuery;
import com.animal.common.command.*;
import com.animal.common.event.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@Saga
@Slf4j
public class RejectApplicationSaga {
    @Autowired
    private transient ReactorCommandGateway commandGateway;
    @Autowired
    private transient ReactorQueryGateway queryGateway;
    @Autowired
    private transient WebClient webClient;
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @StartSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ApplicationRejectedEvent event) {
        log.info("application rejected {}", event);

        queryGateway
                .query(
                        FetchApplicationByIdQuery.builder().applicationId(event.getApplicationId()).build(),
                        ResponseTypes.instanceOf(Application.class)
                )
                .switchIfEmpty(Mono.error(new IllegalArgumentException("failed to find application " + event.getApplicationId())))
                .map(application -> ReleaseAnimalForRejectionCommand
                        .builder()
                        .applicationId(application.getApplicationId())
                        .animalProfileId(application.getAnimalProfileId())
                        .message(event.getMessage())
                        .build()
                )
                .flatMap(command -> commandGateway.send(command))
                .onErrorResume(err -> {
                    log.error("failed to release animal for rejection: {}", err.getMessage());
                    UndoReviewCommand undoReviewCommand = UndoReviewCommand
                            .builder()
                            .applicationId(event.getApplicationId())
                            .message(err.getMessage())
                            .build();
                    return commandGateway.send(undoReviewCommand);
                })
                .subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(AnimalReleasedForRejectionEvent event) {
        log.info("animal released due to rejected application {}", event);
        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId()),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(event.getApplicationId())
                        .applicationStatus(ApplicationStatus.REJECTED)
                        .build()
        );
        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId())
        );
    }

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ReviewUndoneEvent event) {
        log.info("application review undone {}", event);
        RequestReviewCommand requestReviewCommand = RequestReviewCommand.builder().applicationId(event.getApplicationId()).build();
        commandGateway.send(requestReviewCommand).subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ReviewRequestedEvent event){
        log.info("review requested {}", event);
        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId()),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(event.getApplicationId())
                        .applicationStatus(ApplicationStatus.SUBMITTED)
                        .build()
        );
        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId())
        );
    }
}


