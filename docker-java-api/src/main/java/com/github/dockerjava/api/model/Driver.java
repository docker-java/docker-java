package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @since 1.24
     */
    @JsonProperty("Options")
    private Map<String, String> options;

    /**
     * @see #name
     */
    @CheckForNull
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public Driver withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #options
     */
    @CheckForNull
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * @see #options
     */
    public Driver withOptions(Map<String, String> options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", options=" + options +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(name, driver.name) &&
                Objects.equals(options, driver.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, options);
    }
}
