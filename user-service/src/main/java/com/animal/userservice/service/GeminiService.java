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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    public CompletableFuture<List<String>> match(UserProfileForMatch userProfile, List<AnimalProfileForMatch> animalProfiles) {
        Map<String, String> text1 = Map.of("text", "match this user with the list of animals and respond nothing but the value of field 'animalProfileId' of animal profiles in the order from most matched to least delimited by comma only");
        Map<String, Object> text2 = Map.of("text", "this is the user profile: " + JsonUtil.writeValueAsString(userProfile));
        String animalProfileText = animalProfiles.stream().map(JsonUtil::writeValueAsString).collect(Collectors.joining(", ", "[", "]"));
        Map<String, Object> text3 = Map.of("text", "here is the list of animal profiles: " + animalProfileText);
        Map<String, Object> parts = Map.of("parts", List.of(text1, text2, text3));
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

    private List<String> parseResponse(Object response) {
        log.info(response.toString());
        Map<String, Object> responseMap = (Map<String, Object>) response;
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
        Map<String, Object> content = (Map<String, Object>)candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        String text = parts.get(0).get("text").toString();
        String[] ids = text.split(",");
        return Arrays.asList(ids);
    }
}
