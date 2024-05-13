package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class DiscreteResourceSpec extends GenericResource<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Value")
    private Integer value;

    /**
     * @see #value
     */
    @Override
    @CheckForNull
    public Integer getValue() {
        return value;
    }

    /**
     * @see #value
     */
    @Override
    public GenericResource<Integer> withValue(Integer value) {
        this.value = value;
        return this;
    }
}
