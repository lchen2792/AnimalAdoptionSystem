package com.animal.animalservice.command.aggregate;

import com.animal.animalservice.command.model.*;
import com.animal.animalservice.data.model.*;
import com.animal.animalservice.event.model.*;
import com.animal.animalservice.exception.AnimalStatusNotMatchException;
import com.animal.common.command.AdoptAnimalCommand;
import com.animal.common.command.ReleaseAnimalCommand;
import com.animal.common.command.ReleaseAnimalForRejectionCommand;
import com.animal.common.command.ReserveAnimalCommand;
import com.animal.common.event.AnimalAdoptedEvent;
import com.animal.common.event.AnimalReleasedEvent;
import com.animal.common.event.AnimalReleasedForRejectionEvent;
import com.animal.common.event.AnimalReservedEvent;
import com.animal.common.status.AnimalStatus;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Aggregate
@NoArgsConstructor
@Slf4j
public class AnimalAggregate {
    @AggregateIdentifier
    private String animalProfileId;
    private BasicInformation basicInformation;
    private Temperament temperament;
    private CareRequirements careRequirements;
    private List<String> media;
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
        this.media = event.getMedia();
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
        this.medicalConditions = event.getMedicalConditions();
        this.veterinaryRecords = event.getVeterinaryRecords();
    }

    @CommandHandler
    public void handle(ReserveAnimalCommand command) {
        log.info("reserve animal {}", command.getAnimalProfileId());
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
        log.info("reserve animal command processed");
    }

    @EventSourcingHandler
    public void handle(AnimalReservedEvent event){
        log.info("animal reserved event processed");
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(ReleaseAnimalCommand command) {
        log.info("release animal {}", command.getAnimalProfileId());
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
        log.info("release animal command processed");
    }

    @EventSourcingHandler
    public void handle(AnimalReleasedEvent event) {
        log.info("animal released event processed");
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(ReleaseAnimalForRejectionCommand command) {
        log.info("reserve animal {} for rejection", command.getAnimalProfileId());

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
        log.info("reserve animal for rejection command processed");
    }

    @EventSourcingHandler
    public void handle(AnimalReleasedForRejectionEvent event) {
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(AdoptAnimalCommand command) {
        log.info("adopt animal {}", command.getAnimalProfileId());

        if (!AnimalStatus.RESERVED.equals(this.status)) {
            throw new AnimalStatusNotMatchException(command.getAnimalProfileId(), AnimalStatus.RESERVED, this.status);
        }

        AggregateLifecycle.apply(AnimalAdoptedEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .userProfileId(command.getUserProfileId())
                .applicationId(command.getApplicationId())
                .status(AnimalStatus.ADOPTED)
                .build());
        log.info("adopt animal command processed");
    }

    @EventSourcingHandler
    public void handle(AnimalAdoptedEvent event) {
        log.info("animal adopted event processed");
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(DeleteAnimalCommand command){
        log.info("delete animal {}", command.getAnimalProfileId());
        AnimalDeletedEvent event = AnimalDeletedEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .build();
        AggregateLifecycle.apply(event);
        log.info("delete animal command processed");
    }

    @EventSourcingHandler
    public void handle(AnimalDeletedEvent event){
        AggregateLifecycle.markDeleted();
    }

    @CommandHandler
    public void handle(UploadAnimalMediaCommand command) {
        log.info("update animal {} media {}", command.getAnimalProfileId(), command.getMediaId());
        AnimalMediaUploadedEvent event = AnimalMediaUploadedEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .mediaId(command.getMediaId())
                .build();

        AggregateLifecycle.apply(event);
        log.info("update animal media processed");
    }

    @EventSourcingHandler
    public void handle(AnimalMediaUploadedEvent event) {
        if (!this.media.contains(event.getMediaId())) {
            this.media.add(event.getMediaId());
        }
    }

    @CommandHandler
    public void handle(DeleteAnimalMediaCommand command) {
        log.info("delete animal {} media {}", command.getAnimalProfileId(), command.getMediaId());
        AnimalMediaDeletedEvent event = AnimalMediaDeletedEvent
                .builder()
                .animalProfileId(command.getAnimalProfileId())
                .mediaId(command.getMediaId())
                .build();

        AggregateLifecycle.apply(event);
        log.info("delete animal media processed");
    }

    @EventSourcingHandler
    public void handle(AnimalMediaDeletedEvent event) {
        this.media.remove(event.getMediaId());
    }
}
