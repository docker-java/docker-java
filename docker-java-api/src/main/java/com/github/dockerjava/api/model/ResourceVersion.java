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
public class ResourceVersion implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Index")
    private Long index;

    /**
     * @see #index
     */
    @CheckForNull
    public Long getIndex() {
        return index;
    }

    /**
     * @see #index
     */
    public ResourceVersion withIndex(Long index) {
        this.index = index;
        return this;
    }
}
