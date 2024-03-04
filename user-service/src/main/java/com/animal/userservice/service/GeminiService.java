package com.animal.userservice.service;

import com.animal.userservice.controller.model.AnimalProfileForMatch;
import com.animal.userservice.controller.model.UserProfileForMatch;
import com.animal.userservice.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GeminiService {
    @Value("${gemini.api.key}")
    private String secretKey;
    @Value("${gemini.base-url}")
    private String baseUrl;
    @Autowired
    private WebClient webClient;

    public CompletableFuture<Optional<Map<String, Double>>> match(UserProfileForMatch userProfile, List<AnimalProfileForMatch> animalProfiles) {
        Map<String, String> text1 = Map.of("text", "Order the list of animals according to how closely they match this user");
        Map<String, String> text2 = Map.of("text", "This is the user profile: " + JsonUtil.writeValueAsString(userProfile));
        String animalProfileText = animalProfiles.stream().map(JsonUtil::writeValueAsString).collect(Collectors.joining(", ", "[", "]"));
        Map<String, String> text3 = Map.of("text", "Here is the list of animal profiles: " + animalProfileText);
        Map<String, String> text4 = Map.of("text", "Provide a map with key being the animalProfileId and value being the estimated closeness to the user profile provided");
        Map<String, String> text5 = Map.of("text", "Provide your best estimation based on the information provided. You don't have to match the same metrics but try to correlate relevant information and ignore null values");
        Map<String, String> text6 = Map.of("text", "Please answer with a map only that starts with { and ends with }");
        Map<String, Object> parts = Map.of("parts", List.of(text1, text2, text3, text4, text5, text6));
        Map<String, Object> contents = Map.of("contents", List.of(parts));

        log.info(contents.toString());

        return webClient
                .post()
                .uri(baseUrl + "?key=" + secretKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(contents))
                .retrieve()
                .bodyToMono(Object.class)
                .map(this::parseResponse)
                .toFuture();
    }

    private Optional<Map<String, Double>> parseResponse(Object response) {
        log.info(response.toString());
        return Optional
                .ofNullable((Map<String, Object>) response)
                .map(responseMap ->  (List < Map < String, Object >>) responseMap.get("candidates"))
                .map(candidates -> (Map<String, Object>)candidates.get(0).get("content"))
                .map(content -> (List<Map<String, Object>>) content.get("parts"))
                .map(parts -> parts.get(0).get("text"))
                .map(Object::toString)
                .map(text -> {
                    try {
                        return JsonUtil.readValue(text, Map.class);
                    } catch (RuntimeException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                })
                .map(map -> (Map<String, Double>) map);
    }
}
