package com.github.dockerjava.core.util;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of filters to SwarmNodes lists
 */
public class SwarmNodesFiltersBuilder {

    private Map<String, List<String>> filters = new HashMap<String, List<String>>();

    public SwarmNodesFiltersBuilder() {

    }

    public SwarmNodesFiltersBuilder withFilter(String key, String... value) {
        filters.put(key, Arrays.asList(value));
        return this;
    }

    public SwarmNodesFiltersBuilder withFilter(String key, List<String> value) {
        filters.put(key, value);
        return this;
    }

    public List<String> getFilter(String key) {
        return filters.get(key);
    }

    public SwarmNodesFiltersBuilder withIds(List<String> ids) {
        withFilter("id", ids);
        return this;
    }

    public List<String> getIds() {
        return getFilter("id");
    }

    public SwarmNodesFiltersBuilder withNames(List<String> names) {
        withFilter("name", names);
        return this;
    }

    public SwarmNodesFiltersBuilder withMemberships(List<String> memberships) {
        withFilter("membership", memberships);
        return this;
    }

    public List<String> getMemberships() {
        return getFilter("membership");
    }

    public SwarmNodesFiltersBuilder withRoles(List<String> roles) {
        withFilter("role", roles);
        return this;
    }

    public List<String> getRoles() {
        return getFilter("role");
    }

    public List<String> getNames() {
        return getFilter("names");
    }

    // CHECKSTYLE:OFF
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SwarmNodesFiltersBuilder filters1 = (SwarmNodesFiltersBuilder) o;

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
