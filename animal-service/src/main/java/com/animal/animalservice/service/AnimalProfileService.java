package com.animal.animalservice.service;

import com.animal.animalservice.controller.request.FindAnimalProfilesByCriteriaRequest;
import com.animal.animalservice.data.model.AnimalProfile;
import com.animal.common.status.AnimalStatus;
import com.animal.animalservice.data.repository.AnimalProfileRepository;
import com.animal.animalservice.exception.AnimalProfileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnimalProfileService {
    @Autowired
    private transient AnimalProfileRepository animalProfileRepository;
    @Autowired
    private transient MongoTemplate mongoTemplate;

    public List<AnimalProfile> findByCriteria(FindAnimalProfilesByCriteriaRequest query) {
        log.info("handling query {}", query);
        Criteria criteria = new Criteria().and("status").is(AnimalStatus.OPEN);

        if (!StringUtils.isEmpty(query.getSpecies())) {
            criteria.and("basicInformation.species").is(query.getSpecies());
        }
        if (!StringUtils.isEmpty(query.getBreed())) {
            criteria.and("basicInformation.breed").is(query.getBreed());
        }
        if (query.getAgeMin() != null && query.getAgeMax() != null) {
            criteria.and("basicInformation.age").gte(query.getAgeMin()).lte(query.getAgeMax());
        } else if (query.getAgeMin() != null) {
            criteria.and("basicInformation.age").gte(query.getAgeMin());
        } else if (query.getAgeMax() != null) {
            criteria.and("basicInformation.age").lte(query.getAgeMax());
        }
        if (!StringUtils.isEmpty(query.getGender())) {
            criteria.and("basicInformation.gender").is(query.getGender());
        }
        if (query.getNeutered() != null) {
            criteria.and("basicInformation.neutered").is(query.getNeutered());
        }

        Query fetchAnimalProfilesByCriteriaQuery = new Query(criteria);
        fetchAnimalProfilesByCriteriaQuery.fields()
                .exclude("media")
                .exclude("medicalConditions")
                .exclude("veterinaryRecords");

        return mongoTemplate.find(fetchAnimalProfilesByCriteriaQuery, AnimalProfile.class);
    }

    public AnimalProfile findById(String animalProfileId){
        log.info("handling query {}", animalProfileId);
        return animalProfileRepository
                .findById(animalProfileId)
                .orElseThrow(() -> new AnimalProfileNotFoundException(animalProfileId));
    }
}


