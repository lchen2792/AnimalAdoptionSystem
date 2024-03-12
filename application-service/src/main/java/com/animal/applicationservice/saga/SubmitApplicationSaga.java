package com.animal.applicationservice.saga;

import com.animal.applicationservice.command.model.*;
import com.animal.applicationservice.data.model.ApplicationStatus;
import com.animal.applicationservice.data.model.ApplicationStatusSummary;
import com.animal.applicationservice.event.model.*;
import com.animal.applicationservice.exception.RemoteServiceNotAvailableException;
import com.animal.applicationservice.query.model.FetchApplicationStatusSummaryQuery;
import com.animal.common.command.*;
import com.animal.common.event.*;
import com.animal.common.query.FetchUserPaymentMethodByUserProfileIdQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Saga
@Slf4j
public class SubmitApplicationSaga {
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
        log.info("application created {}", event);
        applicationId = event.getApplicationId();
        userProfileId = event.getUserProfileId();
        animalProfileId = event.getAnimalProfileId();
        ReserveAnimalCommand reserveAnimalCommand = ReserveAnimalCommand
                .builder()
                .animalProfileId(animalProfileId)
                .applicationId(applicationId)
                .userProfileId(userProfileId)
                .build();

        SagaLifecycle.associateWith("animalProfileId", event.getAnimalProfileId());

        commandGateway
                .send(reserveAnimalCommand)
                .onErrorResume(err -> {
                    log.error("failed to reserve animal: {}", err.getMessage());
                    CancelApplicationCommand cancelApplicationCommand = CancelApplicationCommand
                            .builder()
                            .applicationId(applicationId)
                            .message(err.getMessage())
                            .build();
                    return commandGateway.send(cancelApplicationCommand);
                })
                .subscribe();
    }

    @SagaEventHandler(associationProperty = "animalProfileId")
    public void handle(AnimalReservedEvent event){
        log.info("animal reserved {}", event);
        paymentId = UUID.randomUUID().toString();
        queryGateway
                .query(
                        FetchUserPaymentMethodByUserProfileIdQuery.builder().userProfileId(userProfileId).build(),
                        ResponseTypes.instanceOf(String.class)
                )
                .switchIfEmpty(Mono.error(new IllegalArgumentException("user payment method not found")))
                .flatMap(customerId -> webClient
                        .post()
                        .uri("/process-payment/customer/" + customerId)
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
                        .applicationId(applicationId)
                        .paymentId(paymentId)
                        .userProfileId(userProfileId)
                        .customerId(tuple2.getT2())
                        .paymentIntentId(tuple2.getT1())
                        .build()
                )
                .flatMap(processPaymentCommand -> commandGateway.send(processPaymentCommand))
                .onErrorResume(err -> {
                    log.error("failed to process payment: {}", err.getMessage());
                    ReleaseAnimalCommand releaseAnimalCommand = ReleaseAnimalCommand
                            .builder()
                            .applicationId(applicationId)
                            .userProfileId(userProfileId)
                            .animalProfileId(animalProfileId)
                            .message(err.getMessage())
                            .build();
                    return commandGateway.send(releaseAnimalCommand);
                })
                .subscribe();
    }

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(PaymentProcessedEvent event) {
        log.info("payment processed {}", event);

        RequestReviewCommand requestReviewCommand = RequestReviewCommand
                .builder()
                .applicationId(applicationId)
                .paymentId(paymentId)
                .build();

        commandGateway
                .send(requestReviewCommand)
                .onErrorResume(err -> {
                    log.error("failed to request review: {}", err.getMessage());
                    webClient
                            .put()
                            .uri("/cancel-payment/" + paymentIntentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .retryWhen(Retry
                                    .backoff(5, Duration.ofMinutes(1))
                                    .jitter(0.75)
                                    .onRetryExhaustedThrow((spec, signal) -> new RemoteServiceNotAvailableException())
                            )
                            .filter(res -> true)
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("failed to cancel payment " + paymentIntentId)))
                            .onErrorComplete(e -> {
                                log.error(e.getMessage());
                                return true;
                            })
                            .subscribe();

                    ReversePaymentCommand reversePaymentCommand =
                            ReversePaymentCommand
                                    .builder()
                                    .applicationId(applicationId)
                                    .paymentId(paymentId)
                                    .userProfileId(userProfileId)
                                    .message(err.getMessage())
                                    .build();
                    return commandGateway.send(reversePaymentCommand);
                })
                .subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ReviewRequestedEvent event) {
        log.info("review requested {}", event);

        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), applicationId),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(applicationId)
                        .applicationStatus(ApplicationStatus.SUBMITTED)
                        .build()
        );

        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), applicationId)
        );
    }

    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(PaymentReversedEvent event) {
        log.info("payment reversed {}", event);

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

    @SagaEventHandler(associationProperty = "animalProfileId")
    public void handle(AnimalReleasedEvent event) {
        log.info("animal released {}", event);

        CancelApplicationCommand cancelApplicationCommand = CancelApplicationCommand
                .builder()
                .applicationId(applicationId)
                .message(event.getMessage())
                .build();
        commandGateway.send(cancelApplicationCommand).subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ApplicationCancelledEvent event) {
        log.info("application cancelled {}", event);

        queryUpdateEmitter.emit(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), applicationId),
                ApplicationStatusSummary
                        .builder()
                        .applicationId(applicationId)
                        .applicationStatus(ApplicationStatus.CANCELLED)
                        .message(event.getMessage())
                        .build()
        );

        queryUpdateEmitter.complete(
                FetchApplicationStatusSummaryQuery.class,
                query -> Objects.equals(query.getApplicationId(), applicationId)
        );
    }
}


