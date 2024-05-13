package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class NamedResourceSpec extends GenericResource<String> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Value")
    private String value;

    /**
     * @see #value
     */
    @Override
    @CheckForNull
    public String getValue() {
        return value;
    }

    /**
     * @see #value
     */
    @Override
    public GenericResource<String> withValue(String value) {
        this.value = value;
        return this;
    }
}
