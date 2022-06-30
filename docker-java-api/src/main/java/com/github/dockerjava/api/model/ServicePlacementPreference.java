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
public class ServicePlacementPreference extends DockerObject implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.28
     */
    @JsonProperty("Spread")
    private ServicePlacementSpread spread;

    public ServicePlacementSpread getSpread() {
        return spread;
    }

    public ServicePlacementPreference withSpread(ServicePlacementSpread spread) {
        this.spread = spread;
        return this;
    }
}
