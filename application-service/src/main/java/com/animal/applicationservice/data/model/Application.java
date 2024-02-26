package com.animal.applicationservice.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    @Id
    private String applicationId;
    private String userProfileId;
    private String animalProfileId;
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
