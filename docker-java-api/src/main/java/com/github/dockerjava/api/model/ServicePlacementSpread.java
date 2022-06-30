package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_28}
 */
@EqualsAndHashCode
@ToString
public class ServicePlacementSpread extends DockerObject implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.28
     */
    @JsonProperty("SpreadDescriptor")
    private String spreadDescriptor;

    public String getSpreadDescriptor() {
        return spreadDescriptor;
    }

    public ServicePlacementSpread withSpreadDescriptor(String spreadDescriptor) {
        this.spreadDescriptor = spreadDescriptor;
        return this;
    }
}
