package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class BindOptions implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Propagation")
    BindPropagation propagation;

    /**
     * @see #propagation
     */
    public BindPropagation getPropagation() {
        return propagation;
    }

    /**
     * @see #propagation
     */
    public BindOptions withPropagation(BindPropagation propagation) {
        this.propagation = propagation;
        return this;
    }
}
