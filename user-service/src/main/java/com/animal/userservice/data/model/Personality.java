package com.animal.userservice.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Personality {
    private Level sociability;
    private Level activity;
    private Level stability;
    private Level patience;
    private Level motivation;
    private Level adaptability;
    private Level communication;
}
