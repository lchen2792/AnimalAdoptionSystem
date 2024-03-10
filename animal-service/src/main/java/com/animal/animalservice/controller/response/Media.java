package com.animal.animalservice.controller.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

import java.io.InputStream;

@Data
@Builder
public class Media {
    private MediaType mediaType;
    private InputStream inputStream;
}
