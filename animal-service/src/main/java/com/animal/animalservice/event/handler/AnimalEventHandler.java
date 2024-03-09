package com.animal.animalservice.event.handler;

import com.animal.animalservice.event.model.*;
import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.animalservice.data.repository.AnimalProfileRepository;
import com.animal.animalservice.exception.AnimalProfileNotFoundException;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnimalEventHandler {

    @Autowired
    private AnimalProfileRepository animalProfileRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @EventHandler
    public void handle(AnimalCreatedEvent event){
        AnimalProfile animalProfile = new AnimalProfile();
        BeanUtils.copyProperties(event, animalProfile);
        animalProfileRepository.save(animalProfile);
    }

    @EventHandler
    public void handle(AnimalUpdatedEvent event) {
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .ifPresentOrElse(
                        animalProfile -> {
                            BeanUtils.copyProperties(event, animalProfile);
                            animalProfileRepository.save(animalProfile);
                            },
                        () -> {
                            throw new AnimalProfileNotFoundException(event.getAnimalProfileId());
                        }
                );
    }

    @EventHandler
    public void handle(AnimalAdoptedEvent event) {
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .ifPresentOrElse(
                        animalProfile -> {
                            animalProfile.setStatus(event.getStatus());
                            animalProfileRepository.save(animalProfile);
                            },
                        () -> {
                            throw new AnimalProfileNotFoundException(event.getAnimalProfileId());
                        }
                );
    }

    @EventHandler
    public void handle(AnimalDeletedEvent event){
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    animalProfileRepository.delete(animalProfile);
                    return animalProfile;
                })
                .ifPresentOrElse(
                        animalProfile -> animalProfile.getMedia().forEach(mediaId -> gridFsTemplate.delete(new Query().addCriteria(Criteria.where("_id").is(mediaId)))),
                        () -> {throw new AnimalProfileNotFoundException(event.getAnimalProfileId());}
                );
    }

    @EventHandler
    public void handle(AnimalReservedEvent event) {
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .ifPresentOrElse(
                        animalProfile -> {
                            animalProfile.setStatus(event.getStatus());
                            animalProfileRepository.save(animalProfile);
                        },
                        () -> {
                            throw new AnimalProfileNotFoundException(event.getAnimalProfileId());
                        }
                );
    }

    @EventHandler
    public void handle(AnimalReleasedEvent event) {
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .ifPresentOrElse(
                        animalProfile -> {
                            animalProfile.setStatus(event.getStatus());
                            animalProfileRepository.save(animalProfile);
                        },
                        () -> {
                            throw new AnimalProfileNotFoundException(event.getAnimalProfileId());
                        }
                );
    }

    @EventHandler
    public void handle(AnimalReleasedForRejectionEvent event){
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .ifPresentOrElse(
                        animalProfile -> {
                            animalProfile.setStatus(event.getStatus());
                            animalProfileRepository.save(animalProfile);
                        },
                        () -> {
                            throw new AnimalProfileNotFoundException(event.getAnimalProfileId());
                        }
                );
    }

    @EventHandler
    public void handle(AnimalMediaUploadedEvent event) {
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .ifPresentOrElse(
                        animalProfile -> {
                            animalProfile.getMedia().add(event.getMediaId());
                            animalProfileRepository.save(animalProfile);
                        },
                        () -> {
                            throw new AnimalProfileNotFoundException(event.getAnimalProfileId());
                        }
                );
    }

    @EventHandler
    public void handle(AnimalMediaDeletedEvent event) {
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .ifPresentOrElse(
                        animalProfile -> {
                            animalProfile.getMedia().remove(event.getMediaId());
                            animalProfileRepository.save(animalProfile);
                        },
                        () -> {
                            throw new AnimalProfileNotFoundException(event.getAnimalProfileId());
                        }
                );
    }
}
