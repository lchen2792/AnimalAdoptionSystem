package com.animal.animalservice.controller;


import com.animal.animalservice.command.model.*;
import com.animal.animalservice.controller.request.CreateAnimalProfileRequest;
import com.animal.animalservice.controller.request.FindAnimalProfilesByCriteriaRequest;
import com.animal.animalservice.controller.request.UpdateAnimalProfileRequest;
import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.animalservice.query.model.FetchAnimalProfileByIdQuery;
import com.animal.animalservice.query.model.FetchAnimalProfilesByCriteriaQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
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
    public CompletableFuture<String>test() {
        return CompletableFuture.supplyAsync(()-> "test endpoint");
    }

    @QueryMapping
    public CompletableFuture<AnimalProfile> findAnimalProfileById(@Argument String animalProfileId){
           return queryGateway
                   .query(
                           FetchAnimalProfileByIdQuery.builder().animalProfileId(animalProfileId).build(),
                           ResponseTypes.instanceOf(AnimalProfile.class)
                   );
    }

    @QueryMapping
    public CompletableFuture<List<AnimalProfile>> findAnimalProfilesByCriteria(@Argument FindAnimalProfilesByCriteriaRequest request){
        FetchAnimalProfilesByCriteriaQuery query = new FetchAnimalProfilesByCriteriaQuery();
        BeanUtils.copyProperties(request, query);
        return queryGateway
                .query(query, ResponseTypes.multipleInstancesOf(AnimalProfile.class));
    }

    @MutationMapping
    public CompletableFuture<String> createAnimalProfile(@Argument CreateAnimalProfileRequest request){
        CreateAnimalCommand command = CreateAnimalCommand
                .builder()
                .animalProfileId(UUID.randomUUID().toString())
                .build();
        BeanUtils.copyProperties(request, command);
        return commandGateway.send(command).thenApplyAsync(result -> command.getAnimalProfileId());
    }

    @MutationMapping
    public CompletableFuture<String> updateAnimalProfileById(@Argument UpdateAnimalProfileRequest request){
        UpdateAnimalCommand command = new UpdateAnimalCommand();
        BeanUtils.copyProperties(request, command);
        return commandGateway
                .send(command)
                .thenApplyAsync(res -> request.getAnimalProfileId());
    }

    @MutationMapping
    public CompletableFuture<String> deleteAnimalProfileById(@Argument String animalProfileId){
        return commandGateway
                .send(DeleteAnimalCommand.builder().animalProfileId(animalProfileId).build())
                .thenApplyAsync(res -> animalProfileId);
    }

    @MutationMapping
    public CompletableFuture<String> uploadAnimalMedia(@Argument String animalProfileId, @Argument MultipartFile file, @Argument String mediaType) {
        return CompletableFuture
                .supplyAsync(() -> {
                    InputStream inputStream = null;
                    try {
                        inputStream = file.getInputStream();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    DBObject metadata = new BasicDBObject();
                    metadata.put("animalProfileId", animalProfileId);
                    metadata.put("timeStamp", Instant.now().toEpochMilli());
                    metadata.put("mediaType", mediaType);
                    return gridFsTemplate.store(inputStream, metadata).toString();
                })
                .thenComposeAsync(mediaId -> {
                    UploadAnimalMediaCommand command = UploadAnimalMediaCommand
                            .builder()
                            .mediaId(mediaId)
                            .animalProfileId(animalProfileId)
                            .build();
                    return commandGateway.send(command);
                })
                .thenApply(res -> {
                    if (res == null) {
                        log.error("animal aggregate not updated");
                    }
                    return "file uploaded";
                })
                .exceptionally(err -> "file not uploaded");
    }

    @MutationMapping
    public CompletableFuture<String> deleteAnimalMedia(@Argument String animalProfileId, @Argument String mediaId){
        gridFsTemplate.delete(new Query().addCriteria(Criteria.where("_id").is(mediaId)));
        DeleteAnimalMediaCommand command = DeleteAnimalMediaCommand
                .builder()
                .animalProfileId(animalProfileId)
                .mediaId(mediaId)
                .build();
        return commandGateway.send(command).thenApplyAsync(res -> mediaId);
    }
}
