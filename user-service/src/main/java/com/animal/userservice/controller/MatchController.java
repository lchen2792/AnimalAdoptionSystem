package com.animal.userservice.controller;

import com.animal.userservice.controller.model.MatchAnimalRequest;
import com.animal.userservice.exception.MatchingException;
import com.animal.userservice.service.AnimalProfileService;
import com.animal.userservice.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private transient GeminiService geminiService;
    @Autowired
    private transient AnimalProfileService animalProfileService;

    @PostMapping("/animals")
    public CompletableFuture<Map<String, Double>> match(@RequestBody MatchAnimalRequest request){
        return animalProfileService
                .findAnimalProfileByCriteria(request.getRequest())
                .thenComposeAsync(animalProfileForMatches ->
                        geminiService.match(request.getUserProfileForMatch(), animalProfileForMatches)
                )
                .thenApplyAsync(res -> res.orElseThrow(MatchingException::new));
    }
}
