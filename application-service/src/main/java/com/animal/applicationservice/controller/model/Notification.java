package com.animal.applicationservice.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {
    private Boolean heartbeat;
    private Object value;
}
