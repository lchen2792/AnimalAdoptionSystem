package com.animal.animalservice.query.handler;

import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.animalservice.data.model.AnimalStatus;
import com.animal.animalservice.data.repository.AnimalProfileRepository;
import com.animal.animalservice.exception.AnimalProfileNotFoundException;
import com.animal.animalservice.query.model.FetchAnimalProfileByIdQuery;
import com.animal.animalservice.query.model.FetchAnimalProfilesByCriteriaQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Component
@Slf4j
public class AnimalQueryHandler {
    @Autowired
    private transient AnimalProfileRepository animalProfileRepository;
    @Autowired
    private transient MongoTemplate mongoTemplate;

    @QueryHandler
    public List<AnimalProfile> handle(FetchAnimalProfilesByCriteriaQuery query) {
        log.info("handling query {}", query);
        Criteria criteria = new Criteria().and("status").is(AnimalStatus.OPEN);

        if (!StringUtils.isEmpty(query.getSpecies())) {
            criteria.and("species").is(query.getSpecies());
        }
        if (!StringUtils.isEmpty(query.getBreed())) {
            criteria.and("breed").is(query.getBreed());
        }
        if (query.getAgeMin() != null && query.getAgeMax() != null) {
            criteria.and("age").gte(query.getAgeMin()).lte(query.getAgeMax());
        } else if (query.getAgeMin() != null) {
            criteria.and("age").gte(query.getAgeMin());
        } else if (query.getAgeMax() != null) {
            criteria.and("age").lte(query.getAgeMax());
        }
        if (!StringUtils.isEmpty(query.getGender())) {
            criteria.and("gender").is(query.getGender());
        }
        if (query.getNeutered() != null) {
            criteria.and("neutered").is(query.getNeutered());
        }

        Query fetchAnimalProfilesByCriteriaQuery = new Query(criteria);
        fetchAnimalProfilesByCriteriaQuery.fields()
                .exclude("media")
                .exclude("medicalConditions")
                .exclude("veterinaryRecords");

        return mongoTemplate.find(fetchAnimalProfilesByCriteriaQuery, AnimalProfile.class);
    }

    @QueryHandler
    public AnimalProfile handle(FetchAnimalProfileByIdQuery query){
        log.info("handling query {}", query);
        return animalProfileRepository
                .findById(query.getAnimalProfileId())
                .orElseThrow(() -> new AnimalProfileNotFoundException(query.getAnimalProfileId()));
    }
}

