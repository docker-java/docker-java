package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Objects;

/**
 * Part of {@link InspectImageResponse}
 *
 * @author Dmitry Tretyakov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Override
    public String toString() {
        return "RootFS{" +
                "type='" + type + '\'' +
                ", layers=" + layers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RootFS rootFS = (RootFS) o;
        return Objects.equals(type, rootFS.type) &&
                Objects.equals(layers, rootFS.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, layers);
    }
}
