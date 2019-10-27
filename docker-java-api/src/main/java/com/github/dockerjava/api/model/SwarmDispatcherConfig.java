package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "SwarmDispatcherConfig{" +
                "heartbeatPeriod=" + heartbeatPeriod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmDispatcherConfig that = (SwarmDispatcherConfig) o;
        return Objects.equals(heartbeatPeriod, that.heartbeatPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heartbeatPeriod);
    }
}
