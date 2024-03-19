package com.animal.animalservice.controller;


import com.animal.animalservice.command.model.*;
import com.animal.animalservice.controller.request.CreateAnimalProfileRequest;
import com.animal.animalservice.controller.request.FindAnimalProfilesByCriteriaRequest;
import com.animal.animalservice.controller.request.UpdateAnimalProfileRequest;
import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.animalservice.service.AnimalProfileService;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Controller
@Slf4j
public class AnimalProfileController {
    @Autowired
    private transient AnimalProfileService animalProfileService;
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient GridFsTemplate gridFsTemplate;

    @QueryMapping
    public AnimalProfile findAnimalProfileById(@Argument String animalProfileId){
        return animalProfileService.findById(animalProfileId);
    }

    @QueryMapping
    public List<AnimalProfile> findAnimalProfilesByCriteria(@Argument FindAnimalProfilesByCriteriaRequest request){
        return animalProfileService.findByCriteria(request);
    }

    @MutationMapping
    public CompletableFuture<String> createAnimalProfile(@Argument CreateAnimalProfileRequest request){
        CreateAnimalCommand command = CreateAnimalCommand
                .builder()
                .animalProfileId(UUID.randomUUID().toString())
                .build();
        BeanUtils.copyProperties(request, command);
        return commandGateway.send(command);
    }

    @MutationMapping
    public CompletableFuture<String> updateAnimalProfileById(@Argument UpdateAnimalProfileRequest request){
        UpdateAnimalCommand command = new UpdateAnimalCommand();
        BeanUtils.copyProperties(request, command);
        return commandGateway.send(command);
    }

    @MutationMapping
    public CompletableFuture<String> deleteAnimalProfileById(@Argument String animalProfileId){
        return commandGateway.send(DeleteAnimalCommand.builder().animalProfileId(animalProfileId).build());
    }
}
