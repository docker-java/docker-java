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
public class SwarmDispatcherConfig implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("HeartbeatPeriod")
    private Long heartbeatPeriod;

    /**
     * @see #heartbeatPeriod
     */
    @CheckForNull
    public Long getHeartbeatPeriod() {
        return heartbeatPeriod;
    }

    /**
     * @see #heartbeatPeriod
     */
    public SwarmDispatcherConfig withHeartbeatPeriod(Long heartbeatPeriod) {
        this.heartbeatPeriod = heartbeatPeriod;
        return this;
    }
}
