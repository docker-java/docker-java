package com.github.dockerjava.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;

/**
 * JSON Encoder for the docker --cache-from parameter.
 */
public class CacheFromEncoder {

    private CacheFromEncoder() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String jsonEncode(Collection<String> imageIds) {
        try {
            return MAPPER.writeValueAsString(imageIds);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
