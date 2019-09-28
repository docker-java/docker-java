package com.github.dockerjava.core.util;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON Encoder for docker filters.
 *
 * @author Carlos Sanchez <carlos@apache.org>
 */
public class FiltersEncoder {

    private FiltersEncoder() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String jsonEncode(Map<String, List<String>> mapStringListString) {
        try {
            return MAPPER.writeValueAsString(mapStringListString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
