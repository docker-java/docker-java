package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
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

    @Override
    public String toString() {
        return "BindOptions{" +
                "propagation=" + propagation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BindOptions that = (BindOptions) o;
        return propagation == that.propagation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(propagation);
    }
}
