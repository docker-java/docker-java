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
}
