package com.animal.userservice.controller;

import com.animal.userservice.controller.model.MatchAnimalRequest;
import com.animal.userservice.exception.MatchingException;
import com.animal.userservice.service.AnimalProfileService;
import com.animal.userservice.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private transient GeminiService geminiService;
    @Autowired
    private transient AnimalProfileService animalProfileService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/animals")
    public Map<String, Number> match(
            @RequestBody MatchAnimalRequest request){
        return animalProfileService
                .findAnimalProfileByCriteria(request.getRequest())
                .thenComposeAsync(animalProfileForMatches ->
                        geminiService.match(request.getUserProfileForMatch(), animalProfileForMatches)
                )
                .thenApply(res -> res.orElseThrow(MatchingException::new))
                .join();
    }
}
