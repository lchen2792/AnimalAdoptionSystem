package com.animal.userservice.data.repository;

import com.animal.userservice.data.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

}
