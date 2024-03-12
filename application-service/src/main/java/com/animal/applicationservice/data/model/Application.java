package com.animal.applicationservice.data.model;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Application {
    @Id
    private String applicationId;
    private String userProfileId;
    private String animalProfileId;
    private String paymentId;
    private ApplicationStatus applicationStatus;
    @Version
    private Long version;
    @CreatedDate
    private Long createdDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private Long lastModifiedDate;
}
