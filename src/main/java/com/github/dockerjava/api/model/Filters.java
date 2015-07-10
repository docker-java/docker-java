package com.github.dockerjava.api.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * Representation of Docker filters.
 *
 * @author Carlos Sanchez <carlos@apache.org>
 *
 */
public class Filters {

    private static ObjectMapper OBJECT_MAPPER = new JacksonJaxbJsonProvider().locateMapper(Map.class,
            MediaType.APPLICATION_JSON_TYPE);

    private Map<String, List<String>> filters = new HashMap<String, List<String>>();

    public Filters() {
    }

    /**
     * Constructor.
     * 
     * @param image
     *            image to filter
     * @param container
     *            container to filter
     */
    public Filters(String image, String container) {
        withImage(image);
        withContainer(container);
    }

    public Filters withFilter(String key, String... value) {
        filters.put(key, Arrays.asList(value));
        return this;
    }

    public List<String> getFilter(String key) {
        return filters.get(key);
    }

    public Filters withImage(String... image) {
        filters.put("image", Arrays.asList(image));
        return this;
    }

    public List<String> getImage() {
        return getFilter("image");
    }

    public Filters withContainer(String... container) {
        filters.put("container", Arrays.asList(container));
        return this;
    }

    public List<String> getContainer() {
        return getFilter("container");
    }

    /**
     * Filter by labels
     * 
     * @param labels
     *            string array in the form ["key"] or ["key=value"] or a mix of both
     * @return
     */
    public Filters withLabel(String... labels) {
        filters.put("label", Arrays.asList(labels));
        return this;
    }

    @Override
    public String toString() {
        try {
            return OBJECT_MAPPER.writeValueAsString(filters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
