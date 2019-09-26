package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.List;

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
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
