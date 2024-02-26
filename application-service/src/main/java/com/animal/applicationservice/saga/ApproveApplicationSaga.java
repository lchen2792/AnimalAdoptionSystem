package com.animal.applicationservice.saga;

import com.animal.applicationservice.command.model.AdoptAnimalCommand;
import com.animal.applicationservice.command.model.UndoReviewCommand;
import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.model.ApplicationStatus;
import com.animal.applicationservice.data.model.ApplicationStatusSummary;
import com.animal.applicationservice.event.model.AnimalAdoptedEvent;
import com.animal.applicationservice.event.model.ApplicationApprovedEvent;
import com.animal.applicationservice.event.model.ReviewUndoneEvent;
import com.animal.applicationservice.query.model.FetchApplicationByIdQuery;
import com.animal.applicationservice.query.model.FetchApplicationStatusSummaryQuery;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Saga
public class ApproveApplicationSaga {
    @Autowired
    private transient ReactorCommandGateway commandGateway;
    @Autowired
    private transient ReactorQueryGateway queryGateway;
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @StartSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ApplicationApprovedEvent event){
        queryGateway
                .query(FetchApplicationByIdQuery.class, ResponseTypes.instanceOf(Application.class))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(event.getApplicationId())))
                .map(Application::getAnimalProfileId)
                .flatMap(id -> {
                    AdoptAnimalCommand adoptAnimalCommand = AdoptAnimalCommand
                            .builder()
                            .animalProfileId(id)
                            .build();
                    return commandGateway.send(adoptAnimalCommand);
                })
                .onErrorResume(err -> {
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
    public void handle(AnimalAdoptedEvent event) {
        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId()),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(event.getApplicationId())
                        .applicationStatus(ApplicationStatus.APPROVED)
                        .build()
        );
        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId())
        );
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ReviewUndoneEvent event) {
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
}
