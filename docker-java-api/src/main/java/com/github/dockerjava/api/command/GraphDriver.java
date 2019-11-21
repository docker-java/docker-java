package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
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
    @FieldName("Name")
    private String name;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @FieldName("Data")
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
