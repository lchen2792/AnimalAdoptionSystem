package com.animal.animalservice.event.handler;

import com.animal.animalservice.event.model.AnimalAdoptedEvent;
import com.animal.animalservice.event.model.AnimalCreatedEvent;
import com.animal.animalservice.event.model.AnimalDeletedEvent;
import com.animal.animalservice.event.model.AnimalUpdatedEvent;
import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.animalservice.data.repository.AnimalProfileRepository;
import com.animal.animalservice.exception.AnimalProfileNotFoundException;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnimalEventHandler {

    @Autowired
    private AnimalProfileRepository animalProfileRepository;

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
                .map(animalProfile -> {
                    animalProfileRepository.save(animalProfile);
                    return animalProfile;
                })
                .orElseThrow(() -> new AnimalProfileNotFoundException(event.getAnimalProfileId()));
    }

    @EventHandler
    public void handle(AnimalAdoptedEvent event) {
        animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    animalProfileRepository.save(animalProfile);
                    return animalProfile;
                })
                .orElseThrow(() -> new AnimalProfileNotFoundException(event.getAnimalProfileId()));
    }

    @EventHandler
    public void handle(AnimalDeletedEvent event){
        AnimalProfile deletedAnimal = animalProfileRepository
                .findById(event.getAnimalProfileId())
                .map(animalProfile -> {
                    animalProfileRepository.delete(animalProfile);
                    return animalProfile;
                })
                .orElseThrow(() -> new AnimalProfileNotFoundException(event.getAnimalProfileId()));
    }
}
