package com.animal.applicationservice.controller;

import com.animal.applicationservice.command.model.ApproveApplicationCommand;
import com.animal.applicationservice.command.model.CreateApplicationCommand;
import com.animal.applicationservice.command.model.RejectApplicationCommand;
import com.animal.applicationservice.controller.model.CreateApplicationRequest;
import com.animal.applicationservice.controller.model.ReviewApplicationRequest;
import com.animal.applicationservice.data.model.Application;
import com.animal.applicationservice.data.repository.ApplicationRepository;
import com.animal.applicationservice.exception.ApplicationLimitException;
import com.animal.applicationservice.query.model.FetchApplicationByIdQuery;
import com.animal.applicationservice.query.model.FetchApplicationCountByUserProfileIdQuery;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    private transient ReactorQueryGateway queryGateway;
    @Autowired
    private transient ReactorCommandGateway commandGateway;
    @Autowired
    private transient ApplicationRepository applicationRepository;
    @Autowired
    private transient Flux<ServerSentEvent<String>> reviewNotificationFlux;
    @Value("${application.count.max}")
    private Integer MAX_APPLICATION_COUNT;

    @GetMapping("/{applicationId}")
    public Mono<ResponseEntity<Application>> findApplicationById(String applicationId){
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
    public Mono<Page<Application>> findApplications(@PageableDefault Pageable pageable){
        return applicationRepository
                .findAll(pageable)
                .collectList()
                .zipWith(applicationRepository.count())
                .map(tuple2 -> new PageImpl<Application>(tuple2.getT1(), pageable, tuple2.getT2()));
    }

    @PostMapping
    public Mono<ResponseEntity<String>> submitApplication(@RequestBody CreateApplicationRequest request){
        FetchApplicationCountByUserProfileIdQuery query = FetchApplicationCountByUserProfileIdQuery
                .builder()
                .userProfileId(request.getUserProfileId())
                .build();
        CreateApplicationCommand command = CreateApplicationCommand
                .builder()
                .applicationId(UUID.randomUUID().toString())
                .animalProfileId(request.getAnimalProfileId())
                .userProfileId(request.getUserProfileId())
                .build();

        return queryGateway
                .query(query, ResponseTypes.instanceOf(Long.class))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(request.getUserProfileId())))
                .filter(count -> count > -1)
                .filter(count -> count <= MAX_APPLICATION_COUNT)
                .switchIfEmpty(Mono.error(new ApplicationLimitException()))
                .flatMap(res -> commandGateway.send(command))
                .map(Object::toString)
                .map(ResponseEntity::ok)
                .onErrorResume(ApplicationLimitException.class,
                        err -> Mono.just(ResponseEntity.badRequest().body(err.getMessage()))
                )
                .onErrorResume(IllegalArgumentException.class,
                        err -> Mono.just(ResponseEntity.badRequest().body(err.getMessage()))
                );
    }

    @PutMapping("/review")
    public Mono<ResponseEntity<String>> reviewApplication(@RequestBody ReviewApplicationRequest request){
            return commandGateway
                    .send(request.getApprove()
                            ? ApproveApplicationCommand.builder().applicationId(request.getApplicationId()).build()
                            : RejectApplicationCommand.builder().applicationId(request.getApplicationId()).message(request.getComment()).build()
                    )
                    .switchIfEmpty(Mono.error(new IllegalArgumentException(request.getApplicationId())))
                    .map(res -> ResponseEntity.ok(res.toString()))
                    .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().body(err.getMessage())));
    }

    @GetMapping(value = "/review/notification")
    public Flux<ServerSentEvent<String>> notifyApplicationReview(){
        return reviewNotificationFlux;
    }
}
