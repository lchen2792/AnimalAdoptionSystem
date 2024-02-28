package com.animal.animalservice.command.aggregate;

import com.animal.animalservice.command.model.*;
import com.animal.animalservice.data.model.*;
import com.animal.animalservice.event.model.*;
import com.animal.animalservice.exception.AnimalStatusNotMatchException;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.bson.types.Binary;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Aggregate
@NoArgsConstructor
public class AnimalAggregate {
    @AggregateIdentifier
    private String animalProfileId;
    private BasicInformation basicInformation;
    private Temperament temperament;
    private CareRequirements careRequirements;
    private List<Binary> photos;
    private List<MedicalCondition> medicalConditions;
    private List<VeterinaryRecord> veterinaryRecords;
    private AnimalStatus status;

    @CommandHandler
    public AnimalAggregate(CreateAnimalCommand command) {
        AnimalCreatedEvent event = new AnimalCreatedEvent();
        BeanUtils.copyProperties(command, event);
        event.setStatus(AnimalStatus.OPEN);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void handle(AnimalCreatedEvent event){
        this.animalProfileId = event.getAnimalProfileId();
        this.basicInformation = event.getBasicInformation();
        this.temperament = event.getTemperament();
        this.careRequirements = event.getCareRequirements();
        this.photos = event.getPhotos();
        this.medicalConditions = event.getMedicalConditions();
        this.veterinaryRecords = event.getVeterinaryRecords();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(UpdateAnimalCommand command){
        AnimalUpdatedEvent event = new AnimalUpdatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void handle(AnimalUpdatedEvent event){
        this.animalProfileId = event.getAnimalProfileId();
        this.basicInformation = event.getBasicInformation();
        this.temperament = event.getTemperament();
        this.careRequirements = event.getCareRequirements();
        this.photos = event.getPhotos();
        this.medicalConditions = event.getMedicalConditions();
        this.veterinaryRecords = event.getVeterinaryRecords();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(ReserveAnimalCommand command) {
        if (!this.status.equals(AnimalStatus.OPEN)) {
            throw new AnimalStatusNotMatchException(command.getAnimalProfileId(), AnimalStatus.OPEN, this.status);
        }

        AggregateLifecycle.apply(AnimalReservedEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .applicationId(command.getApplicationId())
                .userProfileId(command.getUserProfileId())
                .status(AnimalStatus.RESERVED)
                .build());
    }

    @EventSourcingHandler
    public void handle(AnimalReservedEvent event){
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(ReleaseAnimalCommand command) {
        if (!AnimalStatus.RESERVED.equals(this.status)) {
            throw new AnimalStatusNotMatchException(command.getAnimalProfileId(), AnimalStatus.RESERVED, this.status);
        }

        AggregateLifecycle.apply(AnimalReleasedEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .userProfileId(command.getUserProfileId())
                .applicationId(command.getApplicationId())
                .status(AnimalStatus.OPEN)
                .build());
    }

    @EventSourcingHandler
    public void handle(AnimalReleasedEvent event) {
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(ReleaseAnimalForRejectionCommand command) {
        if (!AnimalStatus.RESERVED.equals(this.status)) {
            throw new AnimalStatusNotMatchException(command.getAnimalProfileId(), AnimalStatus.RESERVED, this.status);
        }

        AggregateLifecycle.apply(AnimalReleasedForRejectionEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .userProfileId(command.getUserProfileId())
                .applicationId(command.getApplicationId())
                .status(AnimalStatus.OPEN)
                .build());
    }

    @EventSourcingHandler
    public void handle(AnimalReleasedForRejectionEvent event) {
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(AdoptAnimalCommand command) {
        if (!AnimalStatus.RESERVED.equals(this.status)) {
            throw new AnimalStatusNotMatchException(command.getAnimalProfileId(), AnimalStatus.RESERVED, this.status);
        }

        AggregateLifecycle.apply(AnimalReleasedEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .userProfileId(command.getUserProfileId())
                .applicationId(command.getApplicationId())
                .status(AnimalStatus.ADOPTED)
                .build());
    }

    @EventSourcingHandler
    public void handle(AnimalAdoptedEvent event) {
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(DeleteAnimalCommand command){
        AnimalDeletedEvent event = AnimalDeletedEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void handle(AnimalDeletedEvent event){
        AggregateLifecycle.markDeleted();
    }
}
