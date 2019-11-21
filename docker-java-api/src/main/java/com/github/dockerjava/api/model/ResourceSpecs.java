package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class ResourceSpecs implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("MemoryBytes")
    private Long memoryBytes;

    /**
     * @since 1.24
     */
    @FieldName("NanoCPUs")
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
}
