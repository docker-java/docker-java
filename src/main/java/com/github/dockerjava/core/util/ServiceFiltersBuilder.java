package com.github.dockerjava.core.util;

import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of filters to service lists.
 */
@Deprecated
public class ServiceFiltersBuilder {

    private Map<String, List<String>> filters = new HashMap<>();

    public ServiceFiltersBuilder() {
    }

    public ServiceFiltersBuilder withFilter(String key, String... value) {
        filters.put(key, Arrays.asList(value));
        return this;
    }

    public ServiceFiltersBuilder withFilter(String key, List<String> value) {
        filters.put(key, value);
        return this;
    }

    public List<String> getFilter(String key) {
        return filters.get(key);
    }

    public ServiceFiltersBuilder withIds(List<String> ids) {
        withFilter("id", ids);
        return this;
    }

    public List<String> getIds() {
        return getFilter("id");
    }

    public ServiceFiltersBuilder withNames(List<String> names) {
        withFilter("name", names);
        return this;
    }

    public List<String> getNames() {
        return getFilter("names");
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);

    }

    @Override
    public int hashCode() {
        return filters.hashCode();
    }

    public Map<String, List<String>> build() {
        return filters;
    }
}
