package com.animal.userservice.query.handler;

import com.animal.common.query.FetchUserPaymentMethodByUserProfileIdQuery;
import com.animal.userservice.data.model.UserProfile;
import com.animal.userservice.data.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserQueryHandler {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @QueryHandler
    public Mono<String> handle(FetchUserPaymentMethodByUserProfileIdQuery query) {
        log.info("fetch user payment method processed");
        return userProfileRepository
                .findById(query.getUserProfileId())
                .map(UserProfile::getCustomerId)
                .map(Mono::just)
                .orElseGet(Mono::empty);
    }
}
