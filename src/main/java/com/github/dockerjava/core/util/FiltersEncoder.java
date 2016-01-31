package com.github.dockerjava.core.util;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * JSON Encoder for docker filters.
 *
 * @author Carlos Sanchez <carlos@apache.org>
 *
 */
public class FiltersEncoder {
    private FiltersEncoder() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new JacksonJaxbJsonProvider().locateMapper(Map.class,
            MediaType.APPLICATION_JSON_TYPE);

    public static String jsonEncode(Map<String, List<String>> filters) {
        try {
            return OBJECT_MAPPER.writeValueAsString(filters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
