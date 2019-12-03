package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmNodePluginDescription implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Type")
    private String type;

    /**
     * @since 1.24
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @see #type
     */
    @CheckForNull
    public String getType() {
        return type;
    }

    /**
     * @see #type
     */
    public SwarmNodePluginDescription withType(String type) {
        this.type = type;
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
    public SwarmNodePluginDescription withName(String name) {
        this.name = name;
        return this;
    }
}
