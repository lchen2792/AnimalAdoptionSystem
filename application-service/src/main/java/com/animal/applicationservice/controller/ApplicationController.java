package com.animal.applicationservice.controller;

import com.animal.applicationservice.command.model.ApproveApplicationCommand;
import com.animal.applicationservice.command.model.CreateApplicationCommand;
import com.animal.applicationservice.command.model.RejectApplicationCommand;
import com.animal.applicationservice.constant.Constants;
import com.animal.applicationservice.controller.model.CreateApplicationRequest;
import com.animal.applicationservice.controller.model.Notification;
import com.animal.applicationservice.controller.model.ReviewApplicationRequest;
import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.model.ApplicationStatus;
import com.animal.applicationservice.data.model.ApplicationStatusSummary;
import com.animal.applicationservice.data.repository.ApplicationRepository;
import com.animal.applicationservice.exception.ApplicationLimitException;
import com.animal.applicationservice.query.model.FetchApplicationByIdQuery;
import com.animal.applicationservice.query.model.FetchApplicationCountByUserProfileIdQuery;
import com.animal.applicationservice.query.model.FetchApplicationStatusSummaryQuery;
import jakarta.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequestMapping("/applications")
@Slf4j
public class ApplicationController {
    @Autowired
    private transient ReactorQueryGateway queryGateway;
    @Autowired
    private transient ReactorCommandGateway commandGateway;
    @Autowired
    private transient ApplicationRepository applicationRepository;
    @Autowired
    private transient Flux<ServerSentEvent<Notification>> reviewNotificationFlux;
    @Value("${application.count.max}")
    private Integer MAX_APPLICATION_COUNT;

    @GetMapping("/{applicationId}")
    public Mono<ResponseEntity<Application>> findApplicationById(@PathVariable String applicationId){
        return queryGateway
                .query(
                        FetchApplicationByIdQuery.builder().applicationId(applicationId).build(),
                        ResponseTypes.instanceOf(Application.class)
                )
                .switchIfEmpty(Mono.error(new IllegalArgumentException()))
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class,
                        err -> Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @GetMapping
    public Mono<Page<Application>> findApplications(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        return applicationRepository
                .findByApplicationIdNotNull(pageable)
                .collectList()
                .zipWith(applicationRepository.count())
                .map(tuple2 -> new PageImpl<Application>(tuple2.getT1(), pageable, tuple2.getT2()));
    }

    @PostMapping
    public Flux<ApplicationStatusSummary> submitApplication(@RequestBody CreateApplicationRequest request){
        String applicationId = UUID.randomUUID().toString();
        FetchApplicationCountByUserProfileIdQuery query = FetchApplicationCountByUserProfileIdQuery
                .builder()
                .userProfileId(request.getUserProfileId())
                .build();
        CreateApplicationCommand command = CreateApplicationCommand
                .builder()
                .applicationId(applicationId)
                .animalProfileId(request.getAnimalProfileId())
                .userProfileId(request.getUserProfileId())
                .build();
        FetchApplicationStatusSummaryQuery summaryQuery = FetchApplicationStatusSummaryQuery
                .builder()
                .applicationId(applicationId)
                .build();

        return queryGateway
                .query(query, ResponseTypes.instanceOf(Long.class))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(request.getUserProfileId())))
                .filter(count -> count > -1)
                .filter(count -> count <= MAX_APPLICATION_COUNT)
                .switchIfEmpty(Mono.error(new ApplicationLimitException()))
                .flatMap(res -> commandGateway.send(command))
                .thenMany(queryGateway
                        .subscriptionQuery(
                                summaryQuery,
                                ResponseTypes.instanceOf(ApplicationStatusSummary.class),
                                ResponseTypes.instanceOf(ApplicationStatusSummary.class)
                        )
                        .flatMapMany(res -> res
                                .initialResult()
                                .concatWith(res.updates())
                                .timeout(Duration.ofSeconds(30))
                                .doFinally(signal -> res.close()))
                );
    }

    @PutMapping("/review")
    public Flux<ApplicationStatusSummary> reviewApplication(@RequestBody ReviewApplicationRequest request){
        return queryGateway
                .query(
                    FetchApplicationByIdQuery.builder().applicationId(request.getApplicationId()).build(),
                    ResponseTypes.instanceOf(Application.class)
                )
                .switchIfEmpty(Mono.error(new IllegalArgumentException("failed to find application " + request.getApplicationId())))
                .filter(application -> application.getApplicationStatus().equals(ApplicationStatus.SUBMITTED))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("false application status")))
                .flatMap(res -> commandGateway
                        .send(request.getApprove()
                                ? ApproveApplicationCommand.builder().applicationId(request.getApplicationId()).build()
                                : RejectApplicationCommand.builder().applicationId(request.getApplicationId()).message(request.getComment()).build()
                        )
                )
                .thenMany(queryGateway
                        .subscriptionQuery(
                                FetchApplicationStatusSummaryQuery.builder().applicationId(request.getApplicationId()).build(),
                                ResponseTypes.instanceOf(ApplicationStatusSummary.class),
                                ResponseTypes.instanceOf(ApplicationStatusSummary.class)
                        )
                        .flatMapMany(res -> res
                                .initialResult()
                                .concatWith(res.updates())
                                .timeout(Duration.ofSeconds(30))
                                .doFinally((signal) -> res.close())
                        )
                );
    }

    @GetMapping(value = "/review/notification", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Notification>> notifyApplicationReview(){
        Flux<ServerSentEvent<Notification>> heartBeats = Flux
                .interval(Duration.of(20, ChronoUnit.SECONDS))
                .map(sequence -> ServerSentEvent
                        .<Notification>builder()
                        .event(Constants.NOTIFICATION_REVIEW)
                        .data(Notification.builder().heartbeat(true).build())
                        .build());
        return reviewNotificationFlux.mergeWith(heartBeats);
    }
}
