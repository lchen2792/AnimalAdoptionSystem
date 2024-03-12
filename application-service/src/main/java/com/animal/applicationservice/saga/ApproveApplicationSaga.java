package com.animal.applicationservice.saga;

import com.animal.applicationservice.command.model.RequestReviewCommand;
import com.animal.applicationservice.command.model.UndoReviewCommand;
import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.event.model.*;
import com.animal.applicationservice.exception.RemoteServiceNotAvailableException;
import com.animal.applicationservice.query.model.FetchApplicationByIdQuery;
import com.animal.common.command.*;
import com.animal.common.event.*;
import com.animal.common.query.FetchPaymentIntentIdByIdQuery;
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
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Saga
@Slf4j
public class ApproveApplicationSaga {
    @Autowired
    private transient ReactorCommandGateway commandGateway;
    @Autowired
    private transient ReactorQueryGateway queryGateway;
    @Autowired
    private transient WebClient webClient;
    private static String paymentId;

    @StartSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ApplicationApprovedEvent event){
        log.info("application approved {}", event);

        paymentId = event.getPaymentId();
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
                    log.error("failed to adopt animal: {}", err.getMessage());
                    UndoReviewCommand undoReviewCommand = UndoReviewCommand
                            .builder()
                            .applicationId(event.getApplicationId())
                            .message(err.getMessage())
                            .build();
                    return commandGateway.send(undoReviewCommand);
                })
                .subscribe();
    }

    @SagaEventHandler(associationProperty = "animalProfileId")
    public void handle(AnimalAdoptedEvent event) {
        log.info("animal adopted {}", event);
        ConfirmPaymentCommand confirmPaymentCommand = ConfirmPaymentCommand
                .builder()
                .paymentId(paymentId)
                .applicationId(event.getApplicationId())
                .build();

        queryGateway
                .query(
                        FetchPaymentIntentIdByIdQuery.builder().paymentId(paymentId).build(),
                        ResponseTypes.instanceOf(String.class)
                )
                .switchIfEmpty(Mono.error(new IllegalArgumentException("failed to find payment " + paymentId)))
                .flatMap(paymentIntentId -> webClient.put()
                        .uri("/confirm-payment/" + paymentIntentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .retryWhen(Retry
                                .backoff(5, Duration.ofMinutes(1))
                                .jitter(0.75)
                                .onRetryExhaustedThrow((spec, signal) -> new RemoteServiceNotAvailableException())
                        )
                        .filter(res -> true)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("failed to confirm payment " + paymentIntentId)))

                )
                .flatMap(res -> commandGateway.send(confirmPaymentCommand))
                .onErrorResume(err -> {
                    log.error("failed to confirm payment: {}", err.getMessage());
                    ReserveAnimalCommand reserveAnimalCommand = ReserveAnimalCommand
                            .builder()
                            .applicationId(event.getApplicationId())
                            .animalProfileId(event.getAnimalProfileId())
                            .build();
                    return commandGateway.send(reserveAnimalCommand);
                })
                .subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(PaymentConfirmedEvent event) {
        log.info("payment confirmed {}", event);
    }

    @SagaEventHandler(associationProperty =  "applicationId")
    public void handle(AnimalReservedEvent event) {
        log.info("animal adopted reverted to reserved : {}", event);

        UndoReviewCommand undoReviewCommand = UndoReviewCommand
                .builder()
                .applicationId(event.getApplicationId())
                .build();
        commandGateway.send(undoReviewCommand).subscribe();
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "applicationId")
    public void handle(ReviewUndoneEvent event) {
        log.info("application review undone {}", event);
        RequestReviewCommand requestReviewCommand = RequestReviewCommand.builder().applicationId(event.getApplicationId()).build();
    }
}


