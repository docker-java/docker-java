package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;

/**
 * Part of {@link InspectImageResponse} and {@link InspectContainerResponse}
 *
 * @author Kanstantsin Shautsou
 * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
 */
@EqualsAndHashCode
@ToString
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
}
