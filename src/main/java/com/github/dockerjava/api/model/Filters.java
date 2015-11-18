package com.github.dockerjava.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    public Filters withFilter(String key, String... value) {
        filters.put(key, Arrays.asList(value));
        return this;
    }

    public List<String> getFilter(String key) {
        return filters.get(key);
    }

    public Filters withImages(String... image) {
        withFilter("image", image);
        return this;
    }

    public List<String> getImage() {
        return getFilter("image");
    }

    public Filters withContainers(String... container) {
        withFilter("container", container);
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
    public Filters withLabels(String... labels) {
        withFilter("label", labels);
        return this;
    }

    /**
     * Filter by labels
     *
     * @param labels
     *            {@link Map} of labels that contains label keys and values
     * @return
     */
    public Filters withLabels(Map<String, String> labels) {
        withFilter("label", labelsMapToList(labels).toArray(new String[labels.size()]));
        return this;
    }

    private static List<String> labelsMapToList(Map<String, String> labels) {
        List<String> result = new ArrayList<String>();
        for (Entry<String, String> entry : labels.entrySet()) {
            String rest = (entry.getValue() != null & !entry.getValue().isEmpty()) ? "=" + entry.getValue() : "";

            String label = entry.getKey() + rest;

            result.add(label);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Filters filters1 = (Filters) o;

        return filters.equals(filters1.filters);

    }

    @Override
    public int hashCode() {
        return filters.hashCode();
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
