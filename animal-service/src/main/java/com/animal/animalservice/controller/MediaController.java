package com.animal.animalservice.controller;

import com.animal.animalservice.command.model.DeleteAnimalMediaCommand;
import com.animal.animalservice.command.model.UploadAnimalMediaCommand;
import com.animal.animalservice.controller.response.Media;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/media")
@Slf4j
public class MediaController {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFsOperations operations;
    @Autowired
    private transient CommandGateway commandGateway;

    @GetMapping("/{mediaId}")
    public ResponseEntity<Media> getAnimalMediaById(@PathVariable String mediaId, HttpServletResponse response) {
        try {
            GridFSFile file = Optional
                    .ofNullable(gridFsTemplate.findOne(new Query(Criteria.where("_id").is(mediaId))))
                    .orElseThrow(() -> new FileNotFoundException("file not found"));

            String mediaType = Optional
                    .ofNullable(file.getMetadata())
                    .map(metadata -> metadata.get("_contentType").toString())
                    .orElseThrow(() -> new RuntimeException("failed to decide media type"));

            MediaType contentType = MediaType.parseMediaType(mediaType);
            InputStream inputStream = operations.getResource(file).getInputStream();
            return ResponseEntity.ok().body(Media.builder().mediaType(contentType).inputStream(inputStream).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/animals/{animalProfileId}")
    public CompletableFuture<String> uploadAnimalMedia(@PathVariable String animalProfileId, @RequestPart MultipartFile file, @RequestParam String mediaType) {
        return CompletableFuture
                .supplyAsync(() -> {
                    try {
                        DBObject metadata = new BasicDBObject();
                        metadata.put("animalProfileId", animalProfileId);
                        metadata.put("timeStamp", Instant.now().toEpochMilli());
                        return gridFsTemplate.store(file.getInputStream(), RandomStringUtils.randomAlphanumeric(16), mediaType,  metadata).toString();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                })
                .thenApplyAsync(mediaId -> {
                    UploadAnimalMediaCommand command = UploadAnimalMediaCommand
                            .builder()
                            .mediaId(mediaId)
                            .animalProfileId(animalProfileId)
                            .build();
                    commandGateway.send(command);
                    return mediaId;
                })
                .exceptionally(err -> {
                    log.error(err.getMessage());
                    return null;
                });
    }

    @DeleteMapping("/{mediaId}/animals/{animalProfileId}")
    public CompletableFuture<String> deleteAnimalMedia(@PathVariable String animalProfileId, @PathVariable String mediaId){
        gridFsTemplate.delete(new Query().addCriteria(Criteria.where("_id").is(mediaId)));
        DeleteAnimalMediaCommand command = DeleteAnimalMediaCommand
                .builder()
                .animalProfileId(animalProfileId)
                .mediaId(mediaId)
                .build();
        return commandGateway
                .send(command)
                .thenApply(res -> mediaId)
                .exceptionally(err -> {
                    log.error(err.getMessage());
                    return null;
                });
    }
}
