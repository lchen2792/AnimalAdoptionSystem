package com.animal.animalservice.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/media")
@Slf4j
public class MediaController {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFsOperations operations;

    @GetMapping("/{mediaId}")
    public ResponseEntity<InputStream> getMediaById(@PathVariable String mediaId) {
        try {
            GridFSFile file = Optional
                    .ofNullable(gridFsTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(mediaId))))
                    .orElseThrow(() -> new FileNotFoundException("file not found"));

            String mediaType = file.getMetadata().get("mediaType").toString();
            MediaType contentType = MediaType.parseMediaType(mediaType);
            return ResponseEntity.ok().contentType(contentType).body(operations.getResource(file).getInputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
