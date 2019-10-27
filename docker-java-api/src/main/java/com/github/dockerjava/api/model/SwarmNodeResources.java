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
public class SwarmNodeResources implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("NanoCPUs")
    private Long nanoCPUs;

    /**
     * @since 1.24
     */
    @JsonProperty("MemoryBytes")
    private Long memoryBytes;

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
    public SwarmNodeResources withNanoCPUs(Long nanoCPUs) {
        this.nanoCPUs = nanoCPUs;
        return this;
    }

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
    public SwarmNodeResources withMemoryBytes(Long memoryBytes) {
        this.memoryBytes = memoryBytes;
        return this;
    }

    @Override
    public String toString() {
        return "SwarmNodeResources{" +
                "nanoCPUs=" + nanoCPUs +
                ", memoryBytes=" + memoryBytes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmNodeResources that = (SwarmNodeResources) o;
        return Objects.equals(nanoCPUs, that.nanoCPUs) &&
                Objects.equals(memoryBytes, that.memoryBytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nanoCPUs, memoryBytes);
    }
}
