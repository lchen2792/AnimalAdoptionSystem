package com.animal.applicationservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchApplicationCountByUserProfileIdQuery implements Serializable {
    private String userProfileId;
}
