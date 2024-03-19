package com.animal.animalservice.event.handler;

import com.animal.animalservice.event.model.*;
import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.animalservice.data.repository.AnimalProfileRepository;
import com.animal.animalservice.exception.AnimalProfileNotFoundException;
import com.animal.common.event.AnimalAdoptedEvent;
import com.animal.common.event.AnimalReleasedEvent;
import com.animal.common.event.AnimalReleasedForRejectionEvent;
import com.animal.common.event.AnimalReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AnimalEventHandler {

    @Autowired
    private AnimalProfileRepository animalProfileRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @EventHandler
    public void handle(AnimalCreatedEvent event){
        log.info("{}", event);

        AnimalProfile animalProfile = new AnimalProfile();
        BeanUtils.copyProperties(event, animalProfile);
        animalProfileRepository.save(animalProfile);
    }

    @EventHandler
    public void handle(AnimalUpdatedEvent event) {
        log.info("{}", event);

        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    BeanUtils.copyProperties(event, animalProfile);
                    return animalProfileRepository.save(animalProfile);
                })
                .orElseGet(() -> {
                    log.error("animal profile {} not found to update", event.getAnimalProfileId());
                    return null;
                });
    }

    @EventHandler
    public void handle(AnimalAdoptedEvent event) {
        log.info("{}", event);
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    animalProfile.setStatus(event.getStatus());
                    return animalProfileRepository.save(animalProfile);
                })
                .orElseGet(() -> {
                    log.error("animal profile {} not found to adopt", event.getAnimalProfileId());
                    return null;
                });
    }

    @EventHandler
    public void handle(AnimalDeletedEvent event){
        log.info("{}", event);

        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    animalProfileRepository.delete(animalProfile);
                    return animalProfile;
                })
                .map(animalProfile -> {
                    animalProfile
                            .getMedia()
                            .forEach(mediaId -> gridFsTemplate
                                    .delete(new Query().addCriteria(Criteria.where("_id").is(mediaId)))
                            );
                    return animalProfile;
                })
                .orElseGet(() -> {
                    log.error("animal profile {} not found to delete", event.getAnimalProfileId());
                    return null;
                });
    }

    @EventHandler
    public void handle(AnimalReservedEvent event) {
        log.info("{}", event);

        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    animalProfile.setStatus(event.getStatus());
                    return animalProfileRepository.save(animalProfile);
                })
                .orElseGet(() -> {
                    log.error("animal profile {} not found to reserve", event.getAnimalProfileId());
                    return null;
                });
    }

    @EventHandler
    public void handle(AnimalReleasedEvent event) {
        log.info("{}", event);

        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    animalProfile.setStatus(event.getStatus());
                    return animalProfileRepository.save(animalProfile);
                })
                .orElseGet(() -> {
                    log.error("animal profile {} not found to release", event.getAnimalProfileId());
                    return null;
                });
    }

    @EventHandler
    public void handle(AnimalReleasedForRejectionEvent event){
        log.info("{}", event);

        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    animalProfile.setStatus(event.getStatus());
                    return animalProfileRepository.save(animalProfile);
                })
                .orElseGet(() -> {
                    log.error("animal profile {} not found to release for rejection", event.getAnimalProfileId());
                    return null;
                });
    }

    @EventHandler
    public void handle(AnimalMediaUploadedEvent event) {
        log.info("{}", event);

        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    List<String> media = animalProfile.getMedia();
                    if (!media.contains((event.getMediaId()))) {
                        media.add(event.getMediaId());
                    } else {
                        log.warn("media id {} already exists in animal profile {}", event.getMediaId(), animalProfile.getAnimalProfileId());
                    }
                    return animalProfileRepository.save(animalProfile);
                })
                .orElseGet(() -> {
                    log.error("animal profile {} not found for media upload", event.getAnimalProfileId());
                    return null;
                });

    }

    @EventHandler
    public void handle(AnimalMediaDeletedEvent event) {
        log.info("{}", event);

        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile ->  {
                    List<String> media = animalProfile.getMedia();
                    if (media.contains(event.getMediaId())) {
                        media.remove(event.getMediaId());
                    } else {
                        log.warn("media id {} not found in animal profile {}", event.getMediaId(), animalProfile.getAnimalProfileId());
                    }
                    return animalProfileRepository.save(animalProfile);
                })
                .orElseGet(() -> {
                    log.error("animal profile {} not found for media delete", event.getAnimalProfileId());
                    return null;
                });
    }
}
