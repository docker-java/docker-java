package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class ResourceSpecs extends DockerObject implements Serializable {
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
     * @since 1.24
     */
    @JsonProperty("GenericResources")
    private List<GenericResource<?>> genericResources;

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

    /**
     * @see #genericResources
     */
    @CheckForNull
    public List<GenericResource<?>> getGenericResources() {
        return genericResources;
    }

    /**
     * @see #genericResources
     */
    public ResourceSpecs withGenericResources(List<GenericResource<?>> genericResources) {
        this.genericResources = genericResources;
        return this;
    }
}
