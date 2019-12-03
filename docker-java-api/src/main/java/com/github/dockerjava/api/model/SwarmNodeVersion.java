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
public class SwarmNodeVersion implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Index")
    private long index;

    /**
     * @see #index
     */
    @CheckForNull
    public long getIndex() {
        return index;
    }

    /**
     * @see #index
     */
    public SwarmNodeVersion withIndex(long index) {
        this.index = index;
        return this;
    }
}
