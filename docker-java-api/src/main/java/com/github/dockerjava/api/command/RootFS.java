package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.util.List;

/**
 * Part of {@link InspectImageResponse}
 *
 * @author Dmitry Tretyakov
 */
@EqualsAndHashCode
@ToString
public class RootFS {

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Layers")
    private List<String> layers;

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
    public RootFS withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * @see #layers
     */
    @CheckForNull
    public List<String> getLayers() {
        return layers;
    }

    /**
     * @see #layers
     */
    public RootFS withLayers(List<String> layers) {
        this.layers = layers;
        return this;
    }
}
