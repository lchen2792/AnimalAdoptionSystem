package com.animal.animalservice.controller;


import com.animal.animalservice.command.model.*;
import com.animal.animalservice.controller.request.CreateAnimalProfileRequest;
import com.animal.animalservice.controller.request.FindAnimalProfilesByCriteriaRequest;
import com.animal.animalservice.controller.request.UpdateAnimalProfileRequest;
import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.animalservice.exception.AnimalProfileNotFoundException;
import com.animal.animalservice.query.model.FetchAnimalProfileByIdQuery;
import com.animal.animalservice.query.model.FetchAnimalProfilesByCriteriaQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.AggregateDeletedException;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.queryhandling.QueryGateway;
import org.bson.types.ObjectId;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Controller
@Slf4j
public class AnimalProfileController {
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient GridFsTemplate gridFsTemplate;

    @QueryMapping
    public CompletableFuture<AnimalProfile> findAnimalProfileById(@Argument String animalProfileId){
           return queryGateway
                   .query(
                           FetchAnimalProfileByIdQuery.builder().animalProfileId(animalProfileId).build(),
                           ResponseTypes.instanceOf(AnimalProfile.class)
                   )
                   .exceptionally(err -> {
                       log.error("animal profile " + err.getMessage() + " not found");
                       return null;
                   });
    }

    @QueryMapping
    public CompletableFuture<List<AnimalProfile>> findAnimalProfilesByCriteria(@Argument FindAnimalProfilesByCriteriaRequest request){
        FetchAnimalProfilesByCriteriaQuery query = new FetchAnimalProfilesByCriteriaQuery();
        BeanUtils.copyProperties(request, query);
        return queryGateway
                .query(query, ResponseTypes.multipleInstancesOf(AnimalProfile.class))
                .exceptionally(err -> {
                    log.error(err.getMessage());
                    return List.of();
                });
    }

    @MutationMapping
    public CompletableFuture<String> createAnimalProfile(@Argument CreateAnimalProfileRequest request){
        CreateAnimalCommand command = CreateAnimalCommand
                .builder()
                .animalProfileId(UUID.randomUUID().toString())
                .build();
        BeanUtils.copyProperties(request, command);
        return commandGateway.send(command)
                .thenApply(result -> command.getAnimalProfileId())
                .exceptionally(err -> {
                    log.error(err.getMessage());
                    return null;
                });
    }

    @MutationMapping
    public CompletableFuture<String> updateAnimalProfileById(@Argument UpdateAnimalProfileRequest request){
        UpdateAnimalCommand command = new UpdateAnimalCommand();
        BeanUtils.copyProperties(request, command);
        return commandGateway
                .send(command)
                .thenApply(res -> request.getAnimalProfileId())
                .exceptionally(err -> {
                    log.error(err.getMessage());
                    return null;
                });
    }

    @MutationMapping
    public CompletableFuture<String> deleteAnimalProfileById(@Argument String animalProfileId){
        return commandGateway
                .send(DeleteAnimalCommand.builder().animalProfileId(animalProfileId).build())
                .thenApply(res -> animalProfileId)
                .exceptionally(err -> {
                    log.error(err.getMessage());
                    return null;
                });
    }
}
