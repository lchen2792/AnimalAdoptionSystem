package com.animal.userservice.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    @Id
    private String userProfileId;
    private BasicInformation basicInformation;
    private LivingSituation livingSituation;
    private FamilySituation familySituation;
    private Experience experience;
    private Knowledge knowledge;
    private Personality personality;
    private List<Binary> identifications;
    private String customerId;
    private String authEmail;
    @Version
    private Long version;
    @CreatedDate
    private Long createdDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private Long lastModifiedDate;
}
