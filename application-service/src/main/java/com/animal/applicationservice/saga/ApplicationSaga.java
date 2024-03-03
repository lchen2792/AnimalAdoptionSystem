package com.animal.applicationservice.saga;

import com.animal.applicationservice.command.model.*;
import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.model.ApplicationStatus;
import com.animal.applicationservice.data.model.ApplicationStatusSummary;
import com.animal.applicationservice.event.model.*;
import com.animal.applicationservice.exception.RemoteServiceNotAvailableException;
import com.animal.applicationservice.query.model.FetchApplicationByIdQuery;
import com.animal.applicationservice.query.model.FetchApplicationStatusSummaryQuery;
import com.animal.applicationservice.query.model.FetchUserPaymentMethodByUserProfileIdQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.bson.internal.BsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Saga
@Slf4j
public class ApplicationSaga {
    @Autowired
    private transient ReactorCommandGateway commandGateway;
    @Autowired
    private transient ReactorQueryGateway queryGateway;
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;
    @Autowired
    private transient WebClient webClient;
    private static String applicationId;
    private static String userProfileId;
    private static String animalProfileId;
    private static String paymentId;
    private static String paymentIntentId;

    @StartSaga
    @SagaEventHandler(associationProperty="applicationId")
    public void handle(ApplicationCreatedEvent event){
        applicationId = event.getApplicationId();
        userProfileId = event.getUserProfileId();
        animalProfileId = event.getAnimalProfileId();
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
        paymentId = paymentId == null ? UUID.randomUUID().toString() : paymentId;
        queryGateway
                .query(
                        FetchUserPaymentMethodByUserProfileIdQuery.builder().userProfileId(event.getUserProfileId()).build(),
                        ResponseTypes.instanceOf(String.class)
                )
                .flatMap(customerId -> webClient
                        .post()
                        .uri("/process-payment?customerId=" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(String.class)
                        .retryWhen(Retry
                                .backoff(3, Duration.ofSeconds(5))
                                .jitter(0.75)
                                .onRetryExhaustedThrow((spec, signal) -> new RemoteServiceNotAvailableException())
                        )
                        .zipWith(Mono.just(customerId))
                )
                .doOnSuccess(tuple2 -> paymentIntentId = tuple2.getT1())
                .map(tuple2 -> ProcessPaymentCommand
                        .builder()
                        .applicationId(event.getApplicationId())
                        .paymentId(paymentId)
                        .userProfileId(event.getUserProfileId())
                        .customerId(tuple2.getT2())
                        .paymentIntentId(tuple2.getT1())
                        .build()
                )
                .flatMap(processPaymentCommand -> commandGateway.send(processPaymentCommand))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("process payment result not found")))
                .timeout(Duration.of(30, ChronoUnit.SECONDS))
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

        ReviewApplicationCommand reviewApplicationCommand =
                ReviewApplicationCommand.builder().applicationId(event.getApplicationId()).build();
        commandGateway
                .send(reviewApplicationCommand)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("application not found")))
                .onErrorResume(err -> {
                    ReversePaymentCommand reversePaymentCommand =
                            ReversePaymentCommand
                                    .builder()
                                    .applicationId(event.getApplicationId())
                                    .paymentId(event.getPaymentId())
                                    .userProfileId(event.getUserProfileId())
                                    .message(err.getMessage())
                                    .build();
                    return commandGateway.send(reversePaymentCommand);
                })
                .subscribe();
    }

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ApplicationApprovedEvent event){
        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId()),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(event.getApplicationId())
                        .applicationStatus(ApplicationStatus.APPROVED)
                        .build()
        );

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
        webClient.put()
                .uri("/confirm-payment?paymentIntentId=" + paymentIntentId)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean.class)
                .retryWhen(Retry
                        .backoff(5, Duration.ofMinutes(1))
                        .jitter(0.75)
                        .onRetryExhaustedThrow((spec, signal) -> new RemoteServiceNotAvailableException())
                )
                .filter(res -> true)
                .switchIfEmpty(Mono.error(new IllegalArgumentException()))
                .doOnError(err -> log.error(err.getMessage()))
                .subscribe();

        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId())
        );
    }

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ReviewUndoneEvent event) {
        ReversePaymentCommand reversePaymentCommand = ReversePaymentCommand
                .builder()
                .applicationId(applicationId)
                .userProfileId(userProfileId)
                .paymentId(paymentId)
                .message(event.getMessage())
                .build();

        webClient
                .put()
                .uri("/cancel-payment?paymentIntentId=" + paymentIntentId)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean.class)
                .retryWhen(Retry
                        .backoff(5, Duration.ofMinutes(1))
                        .jitter(0.75)
                        .onRetryExhaustedThrow((spec, signal) -> new RemoteServiceNotAvailableException())
                )
                .filter(res -> true)
                .switchIfEmpty(Mono.error(new IllegalArgumentException()))
                .doOnError(err -> log.error(err.getMessage()))
                .subscribe();

        commandGateway.send(reversePaymentCommand).subscribe();
    }

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(PaymentReversedEvent event) {
        ReleaseAnimalCommand releaseAnimalCommand =
                ReleaseAnimalCommand
                        .builder()
                        .animalProfileId(animalProfileId)
                        .message(event.getMessage())
                        .applicationId(applicationId)
                        .userProfileId(userProfileId)
                        .build();

        commandGateway.send(releaseAnimalCommand).subscribe();
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

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ApplicationRejectedEvent event) {
        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId()),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(event.getApplicationId())
                        .applicationStatus(ApplicationStatus.REJECTED)
                        .message(event.getMessage())
                        .build()
        );

        ReleaseAnimalForRejectionCommand releaseAnimalCommand = ReleaseAnimalForRejectionCommand
                .builder()
                .applicationId(applicationId)
                .userProfileId(userProfileId)
                .animalProfileId(animalProfileId)
                .message(event.getMessage())
                .build();

        commandGateway.send(releaseAnimalCommand).subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(AnimalReleasedForRejectionEvent event) {
        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId()),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(event.getApplicationId())
                        .applicationStatus(ApplicationStatus.REJECTED)
                        .message(event.getMessage())
                        .build()
        );
        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), event.getApplicationId())
        );
    }
}


