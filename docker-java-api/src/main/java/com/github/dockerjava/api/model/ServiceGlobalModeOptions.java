package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceGlobalModeOptions implements Serializable {
    public static final long serialVersionUID = 1L;

    // Intentionally left blank, there are no options for this mode

    @Override
    public String toString() {
        return "ServiceGlobalModeOptions{}";
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
