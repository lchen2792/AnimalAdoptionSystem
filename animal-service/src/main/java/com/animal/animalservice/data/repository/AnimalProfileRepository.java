package com.animal.animalservice.data.repository;

import com.animal.animalservice.data.model.AnimalProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalProfileRepository extends MongoRepository<AnimalProfile, String> {
}
