package com.animal.userservice.query.handler;

import com.animal.userservice.data.model.UserProfile;
import com.animal.userservice.data.repository.UserProfileRepository;
import com.animal.userservice.exception.UserProfileNotFoundException;
import com.animal.userservice.query.model.FetchUserProfileByIdQuery;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserQueryHandler {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @EventHandler
    public UserProfile handle(FetchUserProfileByIdQuery query) {
        return userProfileRepository
                .findById(query.getUserId())
                .orElseThrow(() -> new UserProfileNotFoundException(query.getUserId()));
    }
}
