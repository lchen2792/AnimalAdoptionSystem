package com.animal.userservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String str, Class<T> classType){
        try {
            return objectMapper.readValue(str, classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
