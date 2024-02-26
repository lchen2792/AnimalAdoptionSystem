package com.animal.applicationservice.query.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
@Builder
public class FetchApplicationCountByUserProfileIdQuery implements Serializable {
    private String userProfileId;
}
