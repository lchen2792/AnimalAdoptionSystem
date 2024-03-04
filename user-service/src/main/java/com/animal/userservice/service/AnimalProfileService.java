package com.animal.userservice.service;

import com.animal.userservice.controller.model.AnimalProfileForMatch;
import com.animal.userservice.controller.model.FindAnimalProfilesByCriteriaRequest;
import com.animal.userservice.exception.RemoteServiceNotAvailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AnimalProfileService {
    @Autowired
    private HttpGraphQlClient httpGraphQlClient;
    private static final String ANIMAL_SERVICE = "animal-service";

    @Async
    @Retry(name = ANIMAL_SERVICE)
    @CircuitBreaker(name = ANIMAL_SERVICE, fallbackMethod = "findAnimalProfileByCriteriaFallback")
    public CompletableFuture<List<AnimalProfileForMatch>> findAnimalProfileByCriteria(FindAnimalProfilesByCriteriaRequest request){
        String document = """
                query($request: FindAnimalProfilesByCriteriaRequest!) {
                    findAnimalProfilesByCriteria(request: $request) {
                        animalProfileId,
                        status,
                        basicInformation {
                            species,
                            breed,
                            age,
                            gender,
                            size,
                            neutered
                        },
                        temperament {
                            sociability,
                            activity,
                            trainability,
                            stability,
                            aggressivity,
                            independency,
                            adaptability,
                            preyDrive,
                            communication
                        },
                        careRequirements {
                            space,
                            socializing,
                            companionship,
                            exercise,
                            grooming,
                            diet
                        }
                    }
                }
                """;

        return httpGraphQlClient
                .document(document)
                .variable("request", request)
                .retrieve("findAnimalProfilesByCriteria")
                .toEntityList(AnimalProfileForMatch.class)
                .toFuture();
    }

    public CompletableFuture<List<AnimalProfileForMatch>> findAnimalProfileByCriteriaFallback(
            FindAnimalProfilesByCriteriaRequest request,
            Throwable ex) {
        log.error(ex.getMessage());
        return CompletableFuture.failedFuture(new RemoteServiceNotAvailableException());
    }
}
