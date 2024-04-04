package com.animal.userservice.service;

import com.animal.userservice.data.model.UserProfile;
import com.animal.userservice.data.repository.UserProfileRepository;
import com.animal.userservice.exception.UserProfileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile findUserProfileById(String userProfileId){
        return userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new UserProfileNotFoundException(userProfileId));
    }

    public Optional<UserProfile> findUserProfileByAuthEmail(String authEmail) {
        return Optional.ofNullable(authEmail).flatMap(email -> userProfileRepository.findUserProfileByAuthEmail(email));
    }

    public Page<UserProfile> findUserProfiles(Pageable pageable){
        return userProfileRepository.findAll(pageable);
    }

    public String createUserProfile(UserProfile userProfile){
        return userProfileRepository.save(userProfile).getUserProfileId();
    }

    public String updateUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile).getUserProfileId();
    }

    public String deleteUserProfile(String userProfileId) {
        return userProfileRepository
            .findById(userProfileId)
            .map(userProfile -> {
                userProfileRepository.delete(userProfile);
                return userProfile.getUserProfileId();
            })
            .orElseThrow(() -> new UserProfileNotFoundException(userProfileId));
    }
}
