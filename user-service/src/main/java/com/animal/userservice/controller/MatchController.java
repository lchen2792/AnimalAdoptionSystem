package com.animal.userservice.controller;

import com.animal.common.constant.Constants;
import com.animal.userservice.controller.model.MatchAnimalRequest;
import com.animal.userservice.exception.MatchingException;
import com.animal.userservice.service.AnimalProfileService;
import com.animal.userservice.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

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
    public Map<String, Number> match(
            @RequestBody MatchAnimalRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken){
        return animalProfileService
                .findAnimalProfileByCriteria(request.getRequest(), jwtToken)
                .thenComposeAsync(animalProfileForMatches ->
                        geminiService.match(request.getUserProfileForMatch(), animalProfileForMatches)
                )
                .thenApply(res -> res.orElseThrow(MatchingException::new))
                .join();
    }
}
