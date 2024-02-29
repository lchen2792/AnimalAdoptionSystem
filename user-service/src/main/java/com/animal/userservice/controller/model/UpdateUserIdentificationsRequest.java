package com.animal.userservice.controller.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class UpdateUserIdentificationsRequest {
    private String userProfileId;
    private List<MultipartFile> identifications;
}
