package com.animal.applicationservice.event.model;

import com.animal.applicationservice.data.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequestedEvent implements Serializable {
    private String applicationId;
    private String paymentId;
    private ApplicationStatus applicationStatus;
}
