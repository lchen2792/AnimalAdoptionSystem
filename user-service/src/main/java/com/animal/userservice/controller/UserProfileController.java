package com.animal.userservice.controller;

import com.animal.userservice.controller.model.CreateUserProfileRequest;
import com.animal.userservice.controller.model.UpdateUserProfileRequest;
import com.animal.userservice.controller.model.ValidatePaymentMethodRequest;
import com.animal.userservice.data.model.UserProfile;
import com.animal.userservice.service.PaymentValidationService;
import com.animal.userservice.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-profiles")
@Slf4j
public class UserProfileController {
    @Autowired
    private transient UserProfileService userProfileService;
    @Autowired
    private transient PaymentValidationService paymentValidationService;

    @GetMapping("/{userProfileId}")
    public UserProfile findUserProfileById(@PathVariable String userProfileId) {
        return userProfileService.findUserProfileById(userProfileId);
    }

    @GetMapping
    public Page<UserProfile> findUserProfiles(
            @PageableDefault(
                    sort = {"basicInformation.name.firstName", "basicInformation.name.lastName"},
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {
        return userProfileService.findUserProfiles(pageable);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createUserProfile(@ModelAttribute CreateUserProfileRequest request, @RequestPart List<MultipartFile> files){
        UserProfile userProfile = UserProfile.builder().build();
        BeanUtils.copyProperties(request, userProfile);
        List<Binary> identifications = files
                .stream()
                .map(file -> {
                    try {
                        return new Binary(BsonBinarySubType.BINARY, file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
        userProfile.setIdentifications(identifications);
        return userProfileService.createUserProfile(userProfile);
    }

    @PutMapping
    public String updateUserProfile(@RequestBody UpdateUserProfileRequest request){
        UserProfile curUserProfile = userProfileService.findUserProfileById(request.getUserProfileId());
        BeanUtils.copyProperties(request, curUserProfile);
        return userProfileService.updateUserProfile(curUserProfile);
    }

    @PutMapping("/{userProfileId}/id")
    public String updateIdentifications(@PathVariable String userProfileId, @RequestPart List<MultipartFile> files){
        UserProfile curUserProfile = userProfileService.findUserProfileById(userProfileId);
        List<Binary> identifications = files.stream().map(file -> {
            try {
                return new Binary(BsonBinarySubType.BINARY, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        curUserProfile.setIdentifications(identifications);
        return userProfileService.updateUserProfile(curUserProfile);
    }

    @PutMapping("/{userProfileId}/payment")
    public CompletableFuture<String> updatePaymentDetail(@PathVariable String userProfileId, @RequestBody ValidatePaymentMethodRequest paymentDetail) {
        UserProfile curUserProfile = findUserProfileById(userProfileId);
        return paymentValidationService
                .validatePayment(paymentDetail)
                .thenApply(customerId -> {
                    curUserProfile.setCustomerId(customerId);
                    userProfileService.updateUserProfile(curUserProfile);
                    return "payment validated and updated";
                });
    }

    @DeleteMapping("/{userProfileId}")
    public String deleteUserProfile(@PathVariable String userProfileId){
        return userProfileService.deleteUserProfile(userProfileId);
    }
}

