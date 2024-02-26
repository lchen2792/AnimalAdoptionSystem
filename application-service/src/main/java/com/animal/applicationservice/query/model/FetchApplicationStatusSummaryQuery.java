package com.animal.applicationservice.query.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FetchApplicationStatusSummaryQuery implements Serializable {
    private String applicationId;
}
