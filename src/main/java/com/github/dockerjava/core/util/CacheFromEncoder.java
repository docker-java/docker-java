package com.github.dockerjava.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * JSON Encoder for the docker --cache-from parameter.
 */
public class CacheFromEncoder {
    private CacheFromEncoder() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new JacksonJaxbJsonProvider().locateMapper(Collection.class,
            MediaType.APPLICATION_JSON_TYPE);

    public static String jsonEncode(Collection<String> imageIds) {
        try {
            return OBJECT_MAPPER.writeValueAsString(imageIds);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
