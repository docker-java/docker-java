package com.github.dockerjava.api.model;

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

    private Map<String, String[]> filters = new HashMap<String, String[]>();

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
        filters.put(key, value);
        return this;
    }

    public String[] getFilter(String key) {
        return filters.get(key);
    }

    public Filters withImage(String... image) {
        filters.put("image", image);
        return this;
    }

    public String[] getImage() {
        return getFilter("image");
    }

    public Filters withContainer(String... container) {
        filters.put("container", container);
        return this;
    }

    public String[] getContainer() {
        return getFilter("container");
    }

    public Filters withLabel(String label) {
        return withLabel(label, (String[]) null);
    }

    public Filters withLabel(String label, String... value) {
        if (value != null) {
            filters.put(label, value);
        } else {
            filters.put("label", new String[] { label });
        }
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