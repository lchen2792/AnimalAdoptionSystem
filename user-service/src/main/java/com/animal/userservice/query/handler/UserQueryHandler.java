package com.animal.userservice.query.handler;

import com.animal.userservice.data.model.UserProfile;
import com.animal.userservice.data.repository.UserProfileRepository;
import com.animal.userservice.exception.UserProfileNotFoundException;
import com.animal.userservice.query.model.FetchUserPaymentMethodByUserProfileIdQuery;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserQueryHandler {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @EventHandler
    public String handle(FetchUserPaymentMethodByUserProfileIdQuery query) {
        return userProfileRepository
                .findById(query.getUserProfileId())
                .map(UserProfile::getCustomerId)
                .orElseThrow(() -> new UserProfileNotFoundException(query.getUserProfileId()));
    }
}
