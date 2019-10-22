package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.util.Objects;

/**
 * Part of {@link InspectImageResponse} and {@link InspectContainerResponse}
 *
 * @author Kanstantsin Shautsou
 * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphDriver {
    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("Data")
    private GraphData data;


    /**
     * @see #data
     */
    @CheckForNull
    public GraphData getData() {
        return data;
    }

    /**
     * @see #data
     */
    public GraphDriver withData(GraphData data) {
        this.data = data;
        return this;
    }

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
    public GraphDriver withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "GraphDriver{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphDriver that = (GraphDriver) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
