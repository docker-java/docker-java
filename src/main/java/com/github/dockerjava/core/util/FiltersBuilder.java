package com.github.dockerjava.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Representation of Docker filters.
 *
 * @author Carlos Sanchez <carlos@apache.org>
 *
 */
public class FiltersBuilder {

    private Map<String, List<String>> filters = new HashMap<String, List<String>>();

    public FiltersBuilder() {
    }

    public FiltersBuilder withFilter(String key, String... value) {
        filters.put(key, Arrays.asList(value));
        return this;
    }

    public List<String> getFilter(String key) {
        return filters.get(key);
    }

    public FiltersBuilder withImages(String... image) {
        withFilter("image", image);
        return this;
    }

    public List<String> getImage() {
        return getFilter("image");
    }

    public FiltersBuilder withContainers(String... container) {
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
     */
    public FiltersBuilder withLabels(String... labels) {
        withFilter("label", labels);
        return this;
    }

    /**
     * Filter by labels
     *
     * @param labels
     *            {@link Map} of labels that contains label keys and values
     */
    public FiltersBuilder withLabels(Map<String, String> labels) {
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

    // CHECKSTYLE:OFF
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FiltersBuilder filters1 = (FiltersBuilder) o;

        return filters.equals(filters1.filters);

    }

    // CHECKSTYLE:ON

    @Override
    public int hashCode() {
        return filters.hashCode();
    }

    public Map<String, List<String>> build() {
        return filters;
    }
}
