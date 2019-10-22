package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceSpecs implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("MemoryBytes")
    private Long memoryBytes;

    /**
     * @since 1.24
     */
    @JsonProperty("NanoCPUs")
    private Long nanoCPUs;

    /**
     * @see #memoryBytes
     */
    @CheckForNull
    public Long getMemoryBytes() {
        return memoryBytes;
    }

    /**
     * @see #memoryBytes
     */
    public ResourceSpecs withMemoryBytes(long memoryBytes) {
        this.memoryBytes = memoryBytes;
        return this;
    }

    /**
     * @see #nanoCPUs
     */
    @CheckForNull
    public Long getNanoCPUs() {
        return nanoCPUs;
    }

    /**
     * @see #nanoCPUs
     */
    public ResourceSpecs withNanoCPUs(long nanoCPUs) {
        this.nanoCPUs = nanoCPUs;
        return this;
    }

    @Override
    public String toString() {
        return "ResourceSpecs{" +
                "memoryBytes=" + memoryBytes +
                ", nanoCPUs=" + nanoCPUs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceSpecs that = (ResourceSpecs) o;
        return Objects.equals(memoryBytes, that.memoryBytes) &&
                Objects.equals(nanoCPUs, that.nanoCPUs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memoryBytes, nanoCPUs);
    }
}
