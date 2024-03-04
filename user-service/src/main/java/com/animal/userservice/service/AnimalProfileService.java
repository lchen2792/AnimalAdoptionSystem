package com.animal.userservice.service;

import com.animal.userservice.controller.model.AnimalProfileForMatch;
import com.animal.userservice.controller.model.FindAnimalProfilesByCriteriaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AnimalProfileService {
    @Autowired
    private HttpGraphQlClient httpGraphQlClient;

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
}
